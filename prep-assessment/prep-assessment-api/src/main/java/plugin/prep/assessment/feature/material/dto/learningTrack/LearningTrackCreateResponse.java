package plugin.prep.assessment.feature.material.dto.learningTrack;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class LearningTrackCreateResponse {

    private Long id;

    private String code;

    private String title;

    private String description;

}
