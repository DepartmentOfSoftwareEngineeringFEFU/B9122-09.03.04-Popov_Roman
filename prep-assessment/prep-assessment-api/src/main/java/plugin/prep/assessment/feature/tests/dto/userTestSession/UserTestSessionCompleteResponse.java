package plugin.prep.assessment.feature.tests.dto.userTestSession;

import java.time.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class UserTestSessionCompleteResponse {

    private Long id;

    private Long userId;

    private Long testId;

    private OffsetDateTime startAt;

    private OffsetDateTime endAt;

    private Integer totalSecond;

    private Boolean isCompleted;

    private String userLevel;

}
