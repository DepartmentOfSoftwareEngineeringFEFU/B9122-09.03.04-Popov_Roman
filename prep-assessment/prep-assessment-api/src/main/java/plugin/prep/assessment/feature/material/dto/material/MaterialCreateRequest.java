package plugin.prep.assessment.feature.material.dto.material;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class MaterialCreateRequest {

    private String title;

    private String description;

    private String topic;

    private String subtopic;

    private String level;

}
