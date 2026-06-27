package plugin.prep.assessment.feature.tests.dto.userTestSession;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class UserTestSessionCompleteRequest {

    private Long id;

    private Boolean isComplete;

}
