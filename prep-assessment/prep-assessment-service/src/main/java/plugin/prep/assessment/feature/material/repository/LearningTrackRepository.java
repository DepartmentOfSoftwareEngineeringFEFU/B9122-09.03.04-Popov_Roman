package plugin.prep.assessment.feature.material.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.material.entity.*;

@Repository
public interface LearningTrackRepository extends JpaRepository<LearningTracksEntity, Long> {

    boolean existsByCode(String code);

    @Query(value = """
        select learning_track_id
        from learning_track_topics
        where topic_id = :topicId
        """, nativeQuery = true)
    List<Long> findIdsByTopicId(@Param("topicId") Long topicId);

    @Query(value = """
        select topic_id
        from learning_track_topics
        where learning_track_id = :learningTrackId
        """, nativeQuery = true)
    List<Long> findTopicIdsByLearningTrackId(@Param("learningTrackId") Long learningTrackId);

}
