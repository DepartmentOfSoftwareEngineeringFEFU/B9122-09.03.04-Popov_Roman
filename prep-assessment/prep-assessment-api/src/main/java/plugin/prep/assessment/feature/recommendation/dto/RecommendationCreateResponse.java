package plugin.prep.assessment.feature.recommendation.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
@Schema(description = "Рекомендации пользователю")
public class RecommendationCreateResponse {

    private Long userId;

    private String recommendationType;

    private Long targetId;

    private String sourceService;

    private Long topicId;

    private String topicTitle;

    private String subtopic;

    private String reason;

    private Integer priority;

    private String status;

}
