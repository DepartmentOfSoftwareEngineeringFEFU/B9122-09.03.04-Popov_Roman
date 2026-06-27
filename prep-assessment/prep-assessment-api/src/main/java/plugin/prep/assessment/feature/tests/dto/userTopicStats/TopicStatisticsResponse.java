package plugin.prep.assessment.feature.tests.dto.userTopicStats;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class TopicStatisticsResponse {

    private Long topicId;

    private String topicTitle;

    private Integer totalAnswered;

    private Integer correctCount;

    private Integer incorrectCount;

    private Double accuracy;

}
