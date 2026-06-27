package plugin.prep.assessment.feature.material.entity;

import java.time.*;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "learning_tracks")
public class LearningTracksEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "learning_track_topics",
        joinColumns = @JoinColumn(name = "learning_track_id"),
        inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private List<TopicEntity> topics;

    @CreationTimestamp
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

}
