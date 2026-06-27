package plugin.prep.assessment.feature.material.dto.material;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class MaterialGetResponse {

    private Long id;

    private String title;

    private String description;

    private Long fileId;

    private String topicTitle;

    private String subtopic;

    private String level;

    private Boolean isLiked;

}
