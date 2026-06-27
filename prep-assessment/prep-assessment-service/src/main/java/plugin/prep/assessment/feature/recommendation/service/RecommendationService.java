package plugin.prep.assessment.feature.recommendation.service;

import java.math.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;

import jakarta.transaction.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.material.controller.*;
import plugin.prep.assessment.feature.material.dto.material.*;
import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.material.repository.*;
import plugin.prep.assessment.feature.recommendation.dto.*;
import plugin.prep.assessment.feature.recommendation.entity.*;
import plugin.prep.assessment.feature.recommendation.enums.*;
import plugin.prep.assessment.feature.recommendation.mapper.*;
import plugin.prep.assessment.feature.recommendation.repository.*;
import plugin.prep.assessment.feature.tests.dto.userTopicStats.*;
import plugin.prep.errors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private static final Integer LEVEL_RECOMMENDATION = 3;

    // todo: брать у пользователя его уровень
    private static final String TEMPORARY_LEVEL = "JUNIOR";

    /**
     * Минимальное количество ответов, после которого статистику считаем достаточно достоверной.
     * Используется в формуле:
     * Ci = min(1, totalAnswered / N_MIN)
     */
    private static final int N_MIN = 5;

    /**
     * Максимальный порог давности в днях.
     * Используется в формуле:
     * Ri = min(1, Di / D_MAX)
     */
    private static final int D_MAX = 30;

    /**
     * Коэффициент влияния давности.
     * Используется в формуле:
     * Wi = (1 - Ai) * Ci * (1 + λRi)
     */
    private static final BigDecimal LAMBDA = BigDecimal.valueOf(0.3);

    private final RecommendationRepository recommendationRepository;

    private final RecommendationMapper recommendationMapper;

    private final MaterialController materialController;

    private final TopicRepository topicRepository;

    @Transactional
    public List<RecommendationCreateResponse> create(List<UserTopicStatsGetAllResponse> request) {
        if (request == null || request.isEmpty()) {
            return List.of();
        }

        Long userId = request.getFirst().getUserId();

        recommendationRepository.deleteAllByUserId(userId);

        List<RecommendationEntity> recommendations = request.stream()
            .filter(this::isValidStats)
            .map(stats -> new RecommendationCandidate(stats, calculateWeaknessScore(stats)))
            .sorted(Comparator.comparing(RecommendationCandidate::weaknessScore).reversed())
            .limit(LEVEL_RECOMMENDATION)
            .map(candidate -> toEntity(candidate.stats(), candidate.weaknessScore()))
            .toList();

        List<RecommendationEntity> savedRecommendations = recommendationRepository.saveAll(recommendations);

        return savedRecommendations.stream()
            .map(recommendationMapper::toDto)
            .toList();
    }

    public List<RecommendationCreateResponse> getRecommendation(Long userId) {

        Limit limit = Limit.of(LEVEL_RECOMMENDATION);
        var recommendations = recommendationRepository.findAllByUserId(userId, limit);

        var response = recommendations.stream()
            .map(recommendationMapper::toDto)
            .toList();
        return response;
    }

    private boolean isValidStats(UserTopicStatsGetAllResponse stats) {
        return stats != null
               && stats.getUserId() != null
               && stats.getTopicId() != null
               && stats.getSubtopic() != null
               && stats.getTotalAnswered() != null
               && stats.getTotalAnswered() > 0
               && stats.getAccuracy() != null;
    }

    private BigDecimal calculateWeaknessScore(UserTopicStatsGetAllResponse stats) {
        BigDecimal accuracy = normalizeAccuracy(stats.getAccuracy());

        BigDecimal errorRate = BigDecimal.ONE.subtract(accuracy);

        BigDecimal confidenceCoefficient = calculateConfidenceCoefficient(stats.getTotalAnswered());

        BigDecimal recencyCoefficient = calculateRecencyCoefficient(stats.getLastAnsweredAt());

        BigDecimal recencyMultiplier = BigDecimal.ONE.add(LAMBDA.multiply(recencyCoefficient));

        return errorRate
            .multiply(confidenceCoefficient)
            .multiply(recencyMultiplier)
            .setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal normalizeAccuracy(BigDecimal accuracy) {
        if (accuracy.compareTo(BigDecimal.ONE) > 0) {
            return accuracy.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        }

        return accuracy;
    }

    private BigDecimal calculateConfidenceCoefficient(Integer totalAnswered) {
        BigDecimal value = BigDecimal.valueOf(totalAnswered)
            .divide(BigDecimal.valueOf(N_MIN), 4, RoundingMode.HALF_UP);

        return value.min(BigDecimal.ONE);
    }

    private BigDecimal calculateRecencyCoefficient(OffsetDateTime lastAnsweredAt) {
        if (lastAnsweredAt == null) {
            return BigDecimal.ZERO;
        }

        long days = ChronoUnit.DAYS.between(lastAnsweredAt, OffsetDateTime.now());

        if (days < 0) {
            days = 0;
        }

        BigDecimal value = BigDecimal.valueOf(days)
            .divide(BigDecimal.valueOf(D_MAX), 4, RoundingMode.HALF_UP);

        return value.min(BigDecimal.ONE);
    }

    private RecommendationEntity toEntity(UserTopicStatsGetAllResponse stats, BigDecimal weaknessScore) {
        var topic = getTopicOrThrow(stats.getTopicId());

        var materialsByTopicAndSubtopic = materialController.getMaterialWithFilters(
            new MaterialGetRequest()
                .setTopicId(stats.getTopicId())
                .setSubtopic(stats.getSubtopic())
                .setUserId(stats.getUserId())
                .setLevel(TEMPORARY_LEVEL)
                .setPageSize(10)
                .setPageNumber(0)
        );

        var materialIds = materialsByTopicAndSubtopic.getContent()
            .stream()
            .map(MaterialGetResponse::getId)
            .toList();

        log.info("MATERIAL IDS {}", materialIds);

        Long materialId = null;

        if (!materialIds.isEmpty()) {
            materialId = materialIds.getFirst();
        }

        return RecommendationEntity.builder()
            .userId(stats.getUserId())
            .recommendationType(RecommendationTypeEnum.MATERIAL)
            .targetId(materialId)
            .sourceService(SourceServiceEnum.MATERIAL_SERVICE)
            .topic(topic)
            .subtopic(stats.getSubtopic())
            .reason(buildReason(stats, weaknessScore))
            .priority(null)
            .build();
    }

    private TopicEntity getTopicOrThrow(Long topicId) {
        return topicRepository.findById(topicId)
            .orElseThrow(() -> Exceptions.badRequest(
                "Тема с id " + topicId + " не найдена"
            ));
    }

    private String buildReason(UserTopicStatsGetAllResponse stats, BigDecimal weaknessScore) {
        return "Тема рекомендована для повторения, так как показатель слабости составляет "
               + weaknessScore
               + ". Точность ответов по теме: "
               + stats.getAccuracy()
               + ", всего ответов: "
               + stats.getTotalAnswered()
               + ".";
    }

    private record RecommendationCandidate(
        UserTopicStatsGetAllResponse stats,
        BigDecimal weaknessScore
    ) {

    }

}
