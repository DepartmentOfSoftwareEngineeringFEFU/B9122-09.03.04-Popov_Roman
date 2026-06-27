package plugin.prep.assessment.feature.material.dto.topic;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class TopicUpdateRequest {

    @NotBlank
    private String title;

    private String description;

}
