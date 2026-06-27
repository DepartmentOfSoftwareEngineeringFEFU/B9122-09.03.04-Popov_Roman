package plugin.prep.assessment.feature.recommendation.controller;

import java.util.*;

import lombok.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.recommendation.api.*;
import plugin.prep.assessment.feature.recommendation.dto.*;
import plugin.prep.assessment.feature.recommendation.service.*;
import plugin.prep.assessment.feature.tests.controller.*;
import plugin.prep.assessment.feature.tests.dto.userTopicStats.*;

@RestController
@RequiredArgsConstructor
public class RecommendationController implements RecommendationApi {

    private final RecommendationService recommendationService;

    private final UserTopicStatsController userTopicStatsController;

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<RecommendationCreateResponse> create(RecommendationCreateRequest request) {
        var requestToAnotherService = new UserTopicStatsGetAllRequest().setUserId(request.getUserId());
        var statistics = userTopicStatsController.getAll(requestToAnotherService);

        return recommendationService.create(statistics);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<RecommendationCreateResponse> getRecommendations(Long userId) {
        return recommendationService.getRecommendation(userId);
    }

}
