package plugin.prep.assessment.feature.tests.api;

import java.util.*;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springdoc.core.annotations.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.dto.userTopicStats.*;

@Tag(name = "User Topic Stats Api", description = "Статистика ответов пользователя")
public interface UserTopicStatsApi {

    @GetMapping("/user-topic-stats/users/{id}")
    @Operation(description = "Получение статистики пользователя по темам")
    List<TopicStatisticsResponse> getUserTopicStats(
        @PathVariable Long id
    );

    @GetMapping("/user-topic-stats")
    @Operation(description = "Получение статистики ответов пользователя по подтемам")
    List<SubtopicStatisticsResponse> getUserTopicStatsByTopic(
        @ParameterObject @ModelAttribute UserTopicStatsGetBySubtopicRequest request
    );

    @GetMapping("/user-topic-stats/all")
    @Operation(description = "Получение всей статистики ответов пользователя по темам и подтемам с фильтрами")
    List<UserTopicStatsGetAllResponse> getAll(@ParameterObject @ModelAttribute UserTopicStatsGetAllRequest request);

}
