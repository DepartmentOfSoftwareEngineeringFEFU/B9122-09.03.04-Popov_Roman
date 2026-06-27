package plugin.prep.assessment.feature.material.dto.material;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class MaterialSetLikeResponse {

    private Long userId;

    private Long materialId;

    private Boolean isLiked;

}
