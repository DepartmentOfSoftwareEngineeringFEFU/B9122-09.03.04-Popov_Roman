package plugin.prep.assessment.feature.tests.dto.userTopicStats;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class TopicWithSubtopicStatisticsResponse {

    private TopicStatisticsResponse topic;

    private List<SubtopicStatisticsResponse> subtopics;

}
