package plugin.prep.assessment.feature.tests.dto.answers;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class AnswerGetResponse {

    private Long id;

    private Long questionId;

    private String text;

    private Boolean isCorrect;

    private String explanation;

}
