package plugin.prep.assessment.feature.tests.dto.userQuestion;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class UserQuestionGetResponse {

    private Long id;

    private Long userId;

    private Long questionId;

    private Long answerId;

    private Boolean isCorrect;

}
