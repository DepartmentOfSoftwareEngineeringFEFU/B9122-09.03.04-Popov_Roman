package plugin.prep.assessment.feature.material.dto.learningTrack;

import java.util.*;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class LearningTrackUpdateRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String title;

    private String description;

    private List<Long> topics;

}
