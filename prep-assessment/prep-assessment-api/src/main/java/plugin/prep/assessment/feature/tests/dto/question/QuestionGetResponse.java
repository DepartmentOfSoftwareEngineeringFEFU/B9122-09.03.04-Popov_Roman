package plugin.prep.assessment.feature.tests.dto.question;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class QuestionGetResponse {

    private Long id;

    private Long testId;

    private String topic;

    private String wordingQuestion;

    private Integer serialNumber;

}
