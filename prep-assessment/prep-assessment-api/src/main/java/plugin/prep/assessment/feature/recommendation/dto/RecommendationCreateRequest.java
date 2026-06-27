package plugin.prep.assessment.feature.recommendation.dto;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class RecommendationCreateRequest {

    private Long userId;

}
