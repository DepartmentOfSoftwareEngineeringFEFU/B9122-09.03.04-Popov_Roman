package plugin.prep.assessment.feature.tests.controller;

import java.util.*;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.api.*;
import plugin.prep.assessment.feature.tests.dto.userTopicStats.*;
import plugin.prep.assessment.feature.tests.service.*;

@RestController
@RequiredArgsConstructor
public class UserTopicStatsController implements UserTopicStatsApi {

    private final UserTopicStatsService userTopicStatsService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<TopicStatisticsResponse> getUserTopicStats(Long id) {
        var resp = userTopicStatsService.getAllTopic(id);
        return resp;
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<SubtopicStatisticsResponse> getUserTopicStatsByTopic(UserTopicStatsGetBySubtopicRequest request) {
        return userTopicStatsService.getAllByTopic(request.getUserId(), request.getTopicId());
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<UserTopicStatsGetAllResponse> getAll(UserTopicStatsGetAllRequest request) {
        return userTopicStatsService.getAll(request);
    }

}
