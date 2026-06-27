package plugin.prep.assessment.feature.tests.dto.question;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class QuestionCreateRequest {

    private Long testId;

    private Long topicId;

    private String subtopic;

    @Schema(description = "'JUNIOR', 'MIDDLE', 'SENIOR'")
    private String grade;

    private String wordingQuestion;

    private Integer serialNumber;

}
