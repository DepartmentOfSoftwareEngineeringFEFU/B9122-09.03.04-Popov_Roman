package plugin.prep.assessment.feature.material.dto.material;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class MaterialTopicGetResponse {

    private Long id;

    private String topicTitle;

}
