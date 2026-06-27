package plugin.prep.assessment.feature.readiness.service;

import static plugin.prep.assessment.feature.readiness.config.ReadinessCalculationConfig.*;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.material.repository.*;
import plugin.prep.assessment.feature.readiness.entity.*;
import plugin.prep.assessment.feature.readiness.enums.*;
import plugin.prep.assessment.feature.readiness.repository.*;
import plugin.prep.assessment.feature.tests.entity.*;
import plugin.prep.assessment.feature.tests.repository.*;

@Service
@RequiredArgsConstructor
public class ReadinessService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private static final int SCALE = 2;

    private final ReadinessIndicatorRepository readinessIndicatorRepository;

    private final LearningTrackRepository learningTrackRepository;

    private final UserTopicStatsRepository userTopicStatsRepository;

    private final UserTestSessionRepository userTestSessionRepository;

    @Transactional
    public ReadinessIndicatorEntity getOrCalculate(Long userId) {
        return readinessIndicatorRepository.findByUserId(userId)
            .orElseGet(() -> calculateAndSave(userId));
    }

    @Transactional
    public ReadinessIndicatorEntity calculateAndSave(Long userId) {
        var learningTrackId = userTestSessionRepository.findLatestCompletedLearningTrackIdByUserId(userId)
            .orElse(null);

        return calculateAndSave(userId, learningTrackId);
    }

    @Transactional
    public ReadinessIndicatorEntity calculateAndSave(Long userId, Long learningTrackId) {
        var resolvedLearningTrackId = learningTrackId;

        if (resolvedLearningTrackId == null) {
            resolvedLearningTrackId = userTestSessionRepository.findLatestCompletedLearningTrackIdByUserId(userId)
                .orElse(null);
        }

        if (resolvedLearningTrackId == null) {
            return saveResult(userId, buildNotEnoughDataResult());
        }

        var learningTrack = learningTrackRepository.findById(resolvedLearningTrackId)
            .orElse(null);

        if (learningTrack == null || learningTrack.getTopics() == null || learningTrack.getTopics().isEmpty()) {
            return saveResult(userId, buildNotEnoughDataResult());
        }

        var topics = learningTrack.getTopics();
        var topicIds = topics.stream()
            .map(TopicEntity::getId)
            .filter(Objects::nonNull)
            .toList();

        if (topicIds.isEmpty()) {
            return saveResult(userId, buildNotEnoughDataResult());
        }

        var statsByTopicId = userTopicStatsRepository.findByUserIdAndTopicIds(userId, topicIds)
            .stream()
            .collect(Collectors.groupingBy(stats -> stats.getTopic().getId()));

        var result = calculate(topics.size(), statsByTopicId);
        return saveResult(userId, result);
    }

    private ReadinessCalculationResult calculate(
        int totalTopicCount,
        Map<Long, List<UserTopicStatsEntity>> statsByTopicId
    ) {
        if (totalTopicCount == 0) {
            return buildNotEnoughDataResult();
        }

        int evaluatedTopicCount = 0;
        int masteredTopicCount = 0;
        int weakTopicCount = 0;

        for (var stats : statsByTopicId.values()) {
            var topicStats = aggregateTopicStats(stats);

            if (!topicStats.isEvaluated()) {
                continue;
            }

            evaluatedTopicCount++;

            if (topicStats.accuracy().compareTo(MASTERED_ACCURACY_THRESHOLD) >= 0) {
                masteredTopicCount++;
            }

            if (topicStats.accuracy().compareTo(CRITICAL_ACCURACY_THRESHOLD) < 0) {
                weakTopicCount++;
            }
        }

        var readinessIndex = calculatePercent(masteredTopicCount, totalTopicCount);
        var coverage = calculatePercent(evaluatedTopicCount, totalTopicCount);
        var readinessStatus = determineReadinessStatus(readinessIndex, coverage, weakTopicCount);

        return new ReadinessCalculationResult(
            readinessIndex,
            readinessStatus,
            evaluatedTopicCount,
            masteredTopicCount,
            weakTopicCount,
            coverage
        );
    }

    private TopicStats aggregateTopicStats(List<UserTopicStatsEntity> stats) {
        var totalAnswered = stats.stream()
            .map(UserTopicStatsEntity::getTotalAnswered)
            .filter(Objects::nonNull)
            .mapToInt(Integer::intValue)
            .sum();

        var correctCount = stats.stream()
            .map(UserTopicStatsEntity::getCorrectCount)
            .filter(Objects::nonNull)
            .mapToInt(Integer::intValue)
            .sum();

        if (totalAnswered == 0) {
            return new TopicStats(totalAnswered, BigDecimal.ZERO);
        }

        var accuracy = BigDecimal.valueOf(correctCount)
            .multiply(ONE_HUNDRED)
            .divide(BigDecimal.valueOf(totalAnswered), SCALE, RoundingMode.HALF_UP);

        return new TopicStats(totalAnswered, accuracy);
    }

    private ReadinessStatus determineReadinessStatus(
        BigDecimal readinessIndex,
        BigDecimal coverage,
        int weakTopicCount
    ) {
        if (coverage.compareTo(MIN_COVERAGE_THRESHOLD) < 0) {
            return ReadinessStatus.NOT_ENOUGH_DATA;
        }

        if (readinessIndex.compareTo(READY_INDEX_THRESHOLD) >= 0 && weakTopicCount == 0) {
            return ReadinessStatus.READY;
        }

        return ReadinessStatus.NOT_READY;
    }

    private ReadinessIndicatorEntity saveResult(
        Long userId,
        ReadinessCalculationResult result
    ) {
        var existingIndicator = readinessIndicatorRepository.findByUserId(userId);
        var indicator = existingIndicator.orElseGet(ReadinessIndicatorEntity::new);
        var previousReadinessIndex = existingIndicator
            .map(ReadinessIndicatorEntity::getReadinessIndex)
            .orElse(null);
        var readinessDelta = calculateDelta(result.readinessIndex(), previousReadinessIndex);

        indicator.setUserId(userId);
        indicator.setReadinessIndex(result.readinessIndex());
        indicator.setPreviousReadinessIndex(previousReadinessIndex);
        indicator.setReadinessDelta(readinessDelta);
        indicator.setReadinessStatus(result.readinessStatus());
        indicator.setProgressStatus(determineProgressStatus(readinessDelta));
        indicator.setEvaluatedTopicCount(result.evaluatedTopicCount());
        indicator.setMasteredTopicCount(result.masteredTopicCount());
        indicator.setWeakTopicCount(result.weakTopicCount());
        indicator.setCoverage(result.coverage());
        indicator.setCalculatedAt(LocalDateTime.now());

        return readinessIndicatorRepository.save(indicator);
    }

    private BigDecimal calculateDelta(
        BigDecimal readinessIndex,
        BigDecimal previousReadinessIndex
    ) {
        if (previousReadinessIndex == null) {
            return null;
        }

        return readinessIndex.subtract(previousReadinessIndex)
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    private ProgressStatus determineProgressStatus(BigDecimal readinessDelta) {
        if (readinessDelta == null) {
            return ProgressStatus.NOT_ENOUGH_DATA;
        }

        if (readinessDelta.compareTo(PROGRESS_DELTA_THRESHOLD) > 0) {
            return ProgressStatus.POSITIVE;
        }

        if (readinessDelta.compareTo(PROGRESS_DELTA_THRESHOLD.negate()) < 0) {
            return ProgressStatus.NEGATIVE;
        }

        return ProgressStatus.STABLE;
    }

    private BigDecimal calculatePercent(
        int count,
        int total
    ) {
        if (total == 0) {
            return BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
        }

        var percent = BigDecimal.valueOf(count)
            .multiply(ONE_HUNDRED)
            .divide(BigDecimal.valueOf(total), SCALE, RoundingMode.HALF_UP);

        return clampPercent(percent);
    }

    private BigDecimal clampPercent(BigDecimal value) {
        return Stream.of(BigDecimal.ZERO, value, ONE_HUNDRED)
            .sorted()
            .skip(1)
            .findFirst()
            .orElse(BigDecimal.ZERO)
            .setScale(SCALE, RoundingMode.HALF_UP);
    }

    private ReadinessCalculationResult buildNotEnoughDataResult() {
        return new ReadinessCalculationResult(
            BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP),
            ReadinessStatus.NOT_ENOUGH_DATA,
            0,
            0,
            0,
            BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP)
        );
    }

    private record TopicStats(
        int totalAnswered,
        BigDecimal accuracy
    ) {

        private boolean isEvaluated() {
            return totalAnswered >= MIN_ANSWERS_PER_TOPIC;
        }

    }

    private record ReadinessCalculationResult(
        BigDecimal readinessIndex,
        ReadinessStatus readinessStatus,
        int evaluatedTopicCount,
        int masteredTopicCount,
        int weakTopicCount,
        BigDecimal coverage
    ) {

    }

}
