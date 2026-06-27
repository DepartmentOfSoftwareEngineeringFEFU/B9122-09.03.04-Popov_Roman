package plugin.prep.assessment.feature.tests.model;

import java.time.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTestSessionCreateResponseModel {

    private Long id;

    private Long userId;

    private Long testId;

    private OffsetDateTime startAt;

}
