package plugin.prep.assessment.feature.tests.entity;

import java.math.*;
import java.time.*;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import plugin.prep.assessment.feature.material.entity.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_topic_stats")
public class UserTopicStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private TopicEntity topic;

    @Column(nullable = false)
    private String subtopic;

    @Column(nullable = false)
    private Integer totalAnswered;

    @Column(nullable = false)
    private Integer correctCount;

    @Column(nullable = false)
    private Integer incorrectCount;

    @Column(nullable = false)
    private BigDecimal accuracy;

    @Column(nullable = false)
    private OffsetDateTime lastAnsweredAt;

    @CreationTimestamp
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

}
