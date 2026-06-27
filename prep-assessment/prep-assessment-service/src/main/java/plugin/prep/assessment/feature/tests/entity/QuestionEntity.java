package plugin.prep.assessment.feature.tests.entity;

import java.time.*;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.tests.enums.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "questions")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private TestEntity test;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private TopicEntity topic;

    @Column(nullable = false)
    private String subtopic;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionDifficultyEnum grade;

    @Column(nullable = false)
    private String wordingQuestion;

    @Column(nullable = false)
    private Integer serialNumber;

    @CreationTimestamp
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

}
