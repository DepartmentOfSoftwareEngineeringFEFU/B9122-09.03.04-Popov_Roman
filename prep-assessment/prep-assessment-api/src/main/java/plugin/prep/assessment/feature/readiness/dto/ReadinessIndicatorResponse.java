package plugin.prep.assessment.feature.readiness.dto;

import java.math.*;
import java.time.*;

import lombok.*;
import lombok.experimental.*;

import plugin.prep.assessment.feature.readiness.enums.*;

@Data
@Accessors(chain = true)
public class ReadinessIndicatorResponse {

    private BigDecimal readinessIndex;

    private BigDecimal previousReadinessIndex;

    private BigDecimal readinessDelta;

    private ReadinessStatus readinessStatus;

    private ProgressStatus progressStatus;

    private Integer evaluatedTopicCount;

    private Integer masteredTopicCount;

    private Integer weakTopicCount;

    private BigDecimal coverage;

    private LocalDateTime calculatedAt;

}
