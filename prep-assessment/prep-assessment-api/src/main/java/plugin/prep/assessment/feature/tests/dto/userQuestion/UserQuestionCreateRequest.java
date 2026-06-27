package plugin.prep.assessment.feature.tests.dto.userQuestion;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class UserQuestionCreateRequest {

    private Long userId;

    private Long questionId;

    private Long answerId;

}
