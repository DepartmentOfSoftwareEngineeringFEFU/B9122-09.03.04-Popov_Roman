package plugin.prep.assessment.feature.material.dto.topic;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class TopicResponse {

    private Long id;

    private String title;

    private String description;

}
