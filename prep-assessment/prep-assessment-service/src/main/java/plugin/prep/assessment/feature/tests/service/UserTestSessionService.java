package plugin.prep.assessment.feature.tests.service;

import java.time.*;

import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import plugin.prep.assessment.feature.recommendation.service.*;
import plugin.prep.assessment.feature.readiness.service.*;
import plugin.prep.assessment.feature.tests.dto.userTopicStats.*;
import plugin.prep.assessment.feature.tests.entity.*;
import plugin.prep.assessment.feature.tests.enums.*;
import plugin.prep.assessment.feature.tests.mapper.*;
import plugin.prep.assessment.feature.tests.model.*;
import plugin.prep.assessment.feature.tests.repository.*;
import plugin.prep.errors.*;

@Service
@RequiredArgsConstructor
public class UserTestSessionService {

    private final UserTestSessionRepository userTestSessionRepository;

    private final TestRepository testRepository;

    private final UserTestSessionMapper userTestSessionMapper;

    private final QuestionRepository questionRepository;

    private final UserQuestionRepository userQuestionRepository;

    private final ReadinessService readinessService;

    private final UserTopicStatsService userTopicStatsService;

    private final RecommendationService recommendationService;

    @Transactional
    public UserTestSessionCreateResponseModel create(
        Long userId,
        Long testId
    ) {
        //todo: проверка юзера
        var test = testRepository
            .findById(testId)
            .orElseThrow(() -> Exceptions.notFound(ErrorCode.TEST_NOT_FOUND.format(testId)));

        var now = OffsetDateTime.now();
        var newUserTestSession = UserTestSessionEntity.builder()
            .userId(userId)
            .test(test)
            .startAt(now)
            .isCompleted(false)
            .isActive(true)
            .build();

        var questionsByTestId = questionRepository.findByTestId(testId);

        if (questionsByTestId.isEmpty()) {
            return new UserTestSessionCreateResponseModel();
        }

        var savedTestSession = userTestSessionRepository.save(newUserTestSession);

        var userTestSessionModel = userTestSessionMapper.toModel(savedTestSession);
        return userTestSessionModel;
    }

    @Transactional
    public UserTestSessionCompleteResponseModel setComplete(
        Long id,
        Boolean isComplete
    ) {
        var userTestSession = userTestSessionRepository
            .findById(id)
            .orElseThrow(() -> Exceptions.notFound(ErrorCode.USER_TEST_SESSION_NOT_FOUND.format(id)));

        if (userTestSession.getIsCompleted()) {
            throw Exceptions.conflict(ErrorCode.USER_TEST_SESSION_ALREADY_COMPLETE.format(id));
        }

        userTestSession.setEndAt(OffsetDateTime.now());
        userTestSession.setIsCompleted(isComplete);
        userTestSession.setIsActive(false);

        var startAt = userTestSession.getStartAt();
        var endAt = userTestSession.getEndAt();
        var totalSecond = Duration.between(startAt, endAt).getSeconds();

        userTestSession.setTotalSecond((int) totalSecond);

        var savedTestSession = userTestSessionRepository.save(userTestSession);
        updateRecommendations(savedTestSession);
        recalculateReadiness(savedTestSession);
        var userLevel = calculateDiagnosticUserLevel(savedTestSession);

        return new UserTestSessionCompleteResponseModel()
            .setId(savedTestSession.getId())
            .setUserId(savedTestSession.getUserId())
            .setTestId(savedTestSession.getTest().getId())
            .setStartAt(savedTestSession.getStartAt())
            .setEndAt(savedTestSession.getEndAt())
            .setTotalSecond(savedTestSession.getTotalSecond())
            .setIsCompleted(savedTestSession.getIsCompleted())
            .setUserLevel(userLevel);
    }

    private String calculateDiagnosticUserLevel(UserTestSessionEntity userTestSession) {
        var test = userTestSession.getTest();

        if (!Boolean.TRUE.equals(userTestSession.getIsCompleted())
            || !TypeTestEnum.DIAGNOSTIC.equals(test.getType())) {
            return null;
        }

        var userQuestions = userQuestionRepository.findByUserIdAndTestId(
            userTestSession.getUserId(),
            test.getId()
        );

        if (userQuestions.isEmpty()) {
            return null;
        }

        var maxScore = userQuestions.stream()
            .map(UserQuestionEntity::getQuestion)
            .map(QuestionEntity::getGrade)
            .mapToInt(this::getQuestionWeight)
            .sum();

        if (maxScore == 0) {
            return null;
        }

        var actualScore = userQuestions.stream()
            .filter(userQuestion -> Boolean.TRUE.equals(userQuestion.getIsCorrect()))
            .map(UserQuestionEntity::getQuestion)
            .map(QuestionEntity::getGrade)
            .mapToInt(this::getQuestionWeight)
            .sum();

        var scoreRatio = (double) actualScore / maxScore;

        if (scoreRatio >= 0.75) {
            return TestGradeEnum.SENIOR.name();
        }

        if (scoreRatio >= 0.45) {
            return TestGradeEnum.MIDDLE.name();
        }

        return TestGradeEnum.JUNIOR.name();
    }

    private void updateRecommendations(UserTestSessionEntity userTestSession) {
        if (!Boolean.TRUE.equals(userTestSession.getIsCompleted())) {
            return;
        }

        var statistics = userTopicStatsService.getAll(
            new UserTopicStatsGetAllRequest().setUserId(userTestSession.getUserId())
        );

        recommendationService.create(statistics);
    }

    private void recalculateReadiness(UserTestSessionEntity userTestSession) {
        if (!Boolean.TRUE.equals(userTestSession.getIsCompleted())) {
            return;
        }

        var learningTrack = userTestSession.getTest().getLearningTrack();
        var learningTrackId = learningTrack == null ? null : learningTrack.getId();

        readinessService.calculateAndSave(userTestSession.getUserId(), learningTrackId);
    }

    private int getQuestionWeight(QuestionDifficultyEnum grade) {
        return switch (grade) {
            case JUNIOR -> 1;
            case MIDDLE -> 2;
            case SENIOR -> 3;
        };
    }

}
