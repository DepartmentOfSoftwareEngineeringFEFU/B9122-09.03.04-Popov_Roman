package plugin.prep.assessment.feature.recommendation.api;

import java.util.*;

import io.swagger.v3.oas.annotations.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.recommendation.dto.*;

public interface RecommendationApi {

    @PostMapping("/recommendations")
    @Operation(description = "Создание рекомендации")
    List<RecommendationCreateResponse> create(@RequestBody RecommendationCreateRequest request);

    @GetMapping("/recommendations/users/{userId}")
    @Operation(description = "Получение рекомендаций после теста")
    List<RecommendationCreateResponse> getRecommendations(@PathVariable Long userId);

}
