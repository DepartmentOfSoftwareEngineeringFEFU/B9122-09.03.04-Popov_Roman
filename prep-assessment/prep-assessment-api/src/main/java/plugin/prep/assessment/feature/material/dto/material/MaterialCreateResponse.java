package plugin.prep.assessment.feature.material.dto.material;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
@Builder
public class MaterialCreateResponse {

    private Long materialId;

    private Long fileId;

}
