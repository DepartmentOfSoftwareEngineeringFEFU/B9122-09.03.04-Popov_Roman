package plugin.prep.assessment.feature.tests.dto.userTestSession;

import java.time.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class UserTestSessionCreateResponse {

    private Long id;

    private Long userId;

    private Long testId;

    private OffsetDateTime startAt;

}
