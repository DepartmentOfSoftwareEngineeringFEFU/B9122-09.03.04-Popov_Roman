package plugin.prep.assessment.feature.tests.service;

import java.util.*;
import java.util.stream.*;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.material.repository.*;
import plugin.prep.assessment.feature.tests.aggregator.*;
import plugin.prep.assessment.feature.tests.dto.userTopicStats.*;
import plugin.prep.assessment.feature.tests.entity.*;
import plugin.prep.assessment.feature.tests.mapper.*;
import plugin.prep.assessment.feature.tests.model.*;
import plugin.prep.assessment.feature.tests.repository.*;
import plugin.prep.errors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTopicStatsService {

    private final UserTopicStatsRepository userTopicStatsRepository;

    private final UserTopicStatsMapper userTopicStatsMapper;

    private final UserTopicStatsAggregator userTopicStatsAggregator;

    private final TopicRepository topicRepository;

    public List<UserTopicStatsEntity> getUserTopic(UserTopicStatsGetRequestModel requestModel) {
        var userTopicStats = userTopicStatsRepository
            .findByUserId(
                requestModel.getUserId(),
                Limit.of(requestModel.getFirstFromTop())
            );

        return userTopicStats;
    }

    public List<SubtopicStatisticsResponse> getAllByTopic(Long userId, Long topicId) {
        var topic = topicRepository.findById(topicId)
            .orElseThrow(() -> Exceptions.badRequest(
                "Топик не найден id=%s".formatted(topicId))
            );
        var statistics = userTopicStatsRepository.findByUserIdAndTopic(userId, topic);
        log.info("{}", statistics.stream().map(UserTopicStatsEntity::getAccuracy).toList());
        var response = statistics.stream()
            .map(userTopicStatsMapper::toGetDto)
            .toList();

        log.info("{}", response);
        return response;
    }

    public List<TopicStatisticsResponse> getAllTopic(Long userId) {
        var statistics = userTopicStatsRepository.findByUserId(userId);

        return statistics.stream()
            .collect(Collectors.groupingBy(
                UserTopicStatsEntity::getTopic,
                LinkedHashMap::new,
                Collectors.toList()
            ))
            .entrySet()
            .stream()
            .map(entry -> userTopicStatsAggregator.buildTopicStatistics(
                entry.getKey().getId(),
                entry.getKey().getTitle(),
                entry.getValue()
            ))
            .toList();
    }

    public List<UserTopicStatsGetAllResponse> getAll(UserTopicStatsGetAllRequest request) {
        var userTopicStats = userTopicStatsRepository.findByUserId(request.getUserId());
        var response = userTopicStats.stream()
            .map(userTopicStatsMapper::toGetAllDto)
            .toList();
        return response;
    }

}
