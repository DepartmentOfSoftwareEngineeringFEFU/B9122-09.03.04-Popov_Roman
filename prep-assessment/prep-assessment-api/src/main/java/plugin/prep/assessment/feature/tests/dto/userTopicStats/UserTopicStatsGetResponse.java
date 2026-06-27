package plugin.prep.assessment.feature.tests.dto.userTopicStats;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class UserTopicStatsGetResponse {

    private List<TopicWithSubtopicStatisticsResponse> statistics;

}
