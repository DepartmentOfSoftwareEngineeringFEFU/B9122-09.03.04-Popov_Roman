package plugin.prep.assessment.feature.tests.dto.test;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class TestCreateRequest {

    @NotNull
    private String title;

    private String description;

    @Schema(description = "REGULAR, DIAGNOSTIC")
    private String type;

    private String grade;

    private Long topicId;

    private Long learningTrackId;

}
