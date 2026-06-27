package plugin.prep.assessment.feature.tests.dto.userTopicStats;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class UserTopicStatsGetRequest {

    private Long userId;

    private Integer firstFromTop;

}
