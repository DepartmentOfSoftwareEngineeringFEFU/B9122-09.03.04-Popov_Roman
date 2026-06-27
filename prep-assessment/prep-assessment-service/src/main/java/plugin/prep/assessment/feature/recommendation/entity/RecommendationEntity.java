package plugin.prep.assessment.feature.recommendation.entity;

import java.time.*;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.recommendation.enums.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "recommendations")
public class RecommendationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecommendationTypeEnum recommendationType;

    @Column
    private Long targetId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SourceServiceEnum sourceService;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private TopicEntity topic;

    @Column(nullable = false)
    private String subtopic;

    @Column(nullable = false)
    private String reason;

    @Column
    private Integer priority;

    @CreationTimestamp
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

}
