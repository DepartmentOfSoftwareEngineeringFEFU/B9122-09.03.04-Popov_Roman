package plugin.prep.assessment.feature.readiness.service;

import java.math.*;
import java.time.*;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.material.repository.*;
import plugin.prep.assessment.feature.readiness.entity.*;
import plugin.prep.assessment.feature.readiness.enums.*;
import plugin.prep.assessment.feature.readiness.repository.*;
import plugin.prep.assessment.feature.tests.entity.*;
import plugin.prep.assessment.feature.tests.repository.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadinessServiceTest {

    @Mock
    private ReadinessIndicatorRepository readinessIndicatorRepository;

    @Mock
    private LearningTrackRepository learningTrackRepository;

    @Mock
    private UserTopicStatsRepository userTopicStatsRepository;

    @Mock
    private UserTestSessionRepository userTestSessionRepository;

    @InjectMocks
    private ReadinessService readinessService;

    @BeforeEach
    void setUp() {
        when(readinessIndicatorRepository.save(any(ReadinessIndicatorEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void calculateAndSaveShouldCreateFirstIndicatorWithoutProgressDelta() {
        var userId = 1L;
        var topics = List.of(topic(1L), topic(2L), topic(3L), topic(4L));
        var learningTrack = LearningTracksEntity.builder()
            .id(10L)
            .topics(topics)
            .build();

        when(learningTrackRepository.findById(10L)).thenReturn(Optional.of(learningTrack));
        when(userTopicStatsRepository.findByUserIdAndTopicIds(userId, List.of(1L, 2L, 3L, 4L)))
            .thenReturn(List.of(
                stats(userId, topics.get(0), 3, 3),
                stats(userId, topics.get(1), 3, 1),
                stats(userId, topics.get(2), 2, 2)
            ));
        when(readinessIndicatorRepository.findByUserId(userId)).thenReturn(Optional.empty());

        var result = readinessService.calculateAndSave(userId, 10L);

        assertEquals(new BigDecimal("50.00"), result.getReadinessIndex());
        assertNull(result.getPreviousReadinessIndex());
        assertNull(result.getReadinessDelta());
        assertEquals(ReadinessStatus.NOT_READY, result.getReadinessStatus());
        assertEquals(ProgressStatus.NOT_ENOUGH_DATA, result.getProgressStatus());
        assertEquals(3, result.getEvaluatedTopicCount());
        assertEquals(2, result.getMasteredTopicCount());
        assertEquals(1, result.getWeakTopicCount());
        assertEquals(new BigDecimal("75.00"), result.getCoverage());
        assertNotNull(result.getCalculatedAt());
    }

    @Test
    void calculateAndSaveShouldDetectPositiveProgress() {
        var userId = 1L;
        var topics = List.of(topic(1L), topic(2L), topic(3L), topic(4L));
        var learningTrack = LearningTracksEntity.builder()
            .id(10L)
            .topics(topics)
            .build();
        var existingIndicator = ReadinessIndicatorEntity.builder()
            .userId(userId)
            .readinessIndex(new BigDecimal("25.00"))
            .readinessStatus(ReadinessStatus.NOT_READY)
            .progressStatus(ProgressStatus.NOT_ENOUGH_DATA)
            .evaluatedTopicCount(2)
            .masteredTopicCount(1)
            .weakTopicCount(1)
            .coverage(new BigDecimal("50.00"))
            .calculatedAt(LocalDateTime.now().minusDays(1))
            .build();

        when(learningTrackRepository.findById(10L)).thenReturn(Optional.of(learningTrack));
        when(userTopicStatsRepository.findByUserIdAndTopicIds(userId, List.of(1L, 2L, 3L, 4L)))
            .thenReturn(topics.stream()
                .map(topic -> stats(userId, topic, 3, 3))
                .toList());
        when(readinessIndicatorRepository.findByUserId(userId)).thenReturn(Optional.of(existingIndicator));

        var result = readinessService.calculateAndSave(userId, 10L);

        assertEquals(new BigDecimal("100.00"), result.getReadinessIndex());
        assertEquals(new BigDecimal("25.00"), result.getPreviousReadinessIndex());
        assertEquals(new BigDecimal("75.00"), result.getReadinessDelta());
        assertEquals(ReadinessStatus.READY, result.getReadinessStatus());
        assertEquals(ProgressStatus.POSITIVE, result.getProgressStatus());
        assertEquals(4, result.getEvaluatedTopicCount());
        assertEquals(4, result.getMasteredTopicCount());
        assertEquals(0, result.getWeakTopicCount());
        assertEquals(new BigDecimal("100.00"), result.getCoverage());
    }

    @Test
    void calculateAndSaveShouldUseDiagnosticAnswersAcrossTrackTopics() {
        var userId = 1L;
        var topics = List.of(topic(1L), topic(2L), topic(3L), topic(4L));
        var learningTrack = LearningTracksEntity.builder()
            .id(10L)
            .topics(topics)
            .build();

        when(learningTrackRepository.findById(10L)).thenReturn(Optional.of(learningTrack));
        when(userTopicStatsRepository.findByUserIdAndTopicIds(userId, List.of(1L, 2L, 3L, 4L)))
            .thenReturn(topics.stream()
                .map(topic -> stats(userId, topic, 1, 1))
                .toList());
        when(readinessIndicatorRepository.findByUserId(userId)).thenReturn(Optional.empty());

        var result = readinessService.calculateAndSave(userId, 10L);

        assertEquals(new BigDecimal("100.00"), result.getReadinessIndex());
        assertEquals(ReadinessStatus.READY, result.getReadinessStatus());
        assertEquals(4, result.getEvaluatedTopicCount());
        assertEquals(4, result.getMasteredTopicCount());
        assertEquals(0, result.getWeakTopicCount());
        assertEquals(new BigDecimal("100.00"), result.getCoverage());
    }

    @Test
    void calculateAndSaveShouldReturnNotEnoughDataWhenLearningTrackIsUnknown() {
        var userId = 1L;

        when(userTestSessionRepository.findLatestCompletedLearningTrackIdByUserId(userId)).thenReturn(Optional.empty());
        when(readinessIndicatorRepository.findByUserId(userId)).thenReturn(Optional.empty());

        var result = readinessService.calculateAndSave(userId);

        assertEquals(new BigDecimal("0.00"), result.getReadinessIndex());
        assertEquals(ReadinessStatus.NOT_ENOUGH_DATA, result.getReadinessStatus());
        assertEquals(ProgressStatus.NOT_ENOUGH_DATA, result.getProgressStatus());
        assertEquals(0, result.getEvaluatedTopicCount());
        assertEquals(0, result.getMasteredTopicCount());
        assertEquals(0, result.getWeakTopicCount());
        assertEquals(new BigDecimal("0.00"), result.getCoverage());
    }

    private TopicEntity topic(Long id) {
        return TopicEntity.builder()
            .id(id)
            .title("Topic " + id)
            .build();
    }

    private UserTopicStatsEntity stats(
        Long userId,
        TopicEntity topic,
        int totalAnswered,
        int correctCount
    ) {
        return UserTopicStatsEntity.builder()
            .userId(userId)
            .topic(topic)
            .subtopic("Subtopic")
            .totalAnswered(totalAnswered)
            .correctCount(correctCount)
            .incorrectCount(totalAnswered - correctCount)
            .accuracy(BigDecimal.valueOf(correctCount)
                .divide(BigDecimal.valueOf(totalAnswered), 4, RoundingMode.HALF_UP))
            .lastAnsweredAt(OffsetDateTime.now())
            .build();
    }

}
