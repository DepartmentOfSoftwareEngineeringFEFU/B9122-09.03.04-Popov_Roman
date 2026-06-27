package plugin.prep.assessment.feature.readiness.entity;

import java.math.*;
import java.time.*;

import jakarta.persistence.*;
import lombok.*;

import plugin.prep.assessment.feature.readiness.enums.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_readiness_indicators")
public class ReadinessIndicatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal readinessIndex;

    @Column(precision = 5, scale = 2)
    private BigDecimal previousReadinessIndex;

    @Column(precision = 5, scale = 2)
    private BigDecimal readinessDelta;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReadinessStatus readinessStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    @Column(nullable = false)
    private Integer evaluatedTopicCount;

    @Column(nullable = false)
    private Integer masteredTopicCount;

    @Column(nullable = false)
    private Integer weakTopicCount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal coverage;

    @Column(nullable = false)
    private LocalDateTime calculatedAt;

}
