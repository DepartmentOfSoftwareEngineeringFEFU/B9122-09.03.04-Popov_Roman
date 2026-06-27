package plugin.prep.assessment.feature.tests.dto.userTopicStats;

import java.math.*;
import java.time.*;

import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class UserTopicStatsGetAllResponse {

    private Long userId;

    private Long topicId;

    private String subtopic;

    private Integer totalAnswered;

    private Integer correctCount;

    private Integer incorrectCount;

    private BigDecimal accuracy;

    private OffsetDateTime lastAnsweredAt;

}
