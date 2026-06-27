package plugin.prep.assessment.feature.tests.aggregator;

import java.util.*;

import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.tests.dto.userTopicStats.*;
import plugin.prep.assessment.feature.tests.entity.*;

@Component
public class UserTopicStatsAggregator {

    public TopicStatisticsResponse buildTopicStatistics(
        Long topicId,
        String topic,
        List<UserTopicStatsEntity> entities
    ) {
        var totalAnswered = entities.stream()
            .mapToInt(UserTopicStatsEntity::getTotalAnswered)
            .sum();

        var correctCount = entities.stream()
            .mapToInt(UserTopicStatsEntity::getCorrectCount)
            .sum();

        var incorrectCount = entities.stream()
            .mapToInt(UserTopicStatsEntity::getIncorrectCount)
            .sum();

        var accuracy = calculateAccuracy(
            totalAnswered,
            correctCount
        );

        return new TopicStatisticsResponse()
            .setTopicId(topicId)
            .setTopicTitle(topic)
            .setTotalAnswered(totalAnswered)
            .setCorrectCount(correctCount)
            .setIncorrectCount(incorrectCount)
            .setAccuracy(accuracy);
    }

    private Double calculateAccuracy(
        Integer totalAnswered,
        Integer correctCount
    ) {
        if (totalAnswered == null || totalAnswered == 0) {
            return 0.0;
        }

        return Math.round(((double) correctCount / totalAnswered) * 10000) / 100.0;
    }

}
