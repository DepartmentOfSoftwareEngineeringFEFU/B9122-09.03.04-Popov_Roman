package plugin.prep.assessment.feature.tests.dto.question;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class QuestionResponse {

    private Long id;

    private Long testId;

    private Long topicId;

    private String subtopic;

    private String grade;

    private String wordingQuestion;

    private Integer serialNumber;

}
