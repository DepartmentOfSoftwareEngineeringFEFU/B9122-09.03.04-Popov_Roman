package plugin.prep.assessment.feature.tests.dto.userQuestion;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class UserQuestionGetRequest {

    private Long testId;

}
