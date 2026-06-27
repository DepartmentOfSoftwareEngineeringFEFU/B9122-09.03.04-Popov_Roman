package plugin.prep.assessment.feature.tests.dto.userTopicStats;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class SubtopicStatisticsResponse {

    private String topicTitle;

    private String subtopic;

    private Integer totalAnswered;

    private Integer correctCount;

    private Integer incorrectCount;

    private Double accuracy;

}
