package plugin.prep.assessment.feature.material.dto.learningTrack;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class LearningTrackResponse {

    private Long id;

    private String code;

    private String title;

    private String description;

    private List<Long> topicIds;

}
