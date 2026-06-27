package plugin.prep.assessment.feature.tests.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.tests.entity.*;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long>, JpaSpecificationExecutor<TestEntity> {

    boolean existsByTitle(String title);

    TestEntity findByLearningTrackId(Long id);

    @Query(value = """
        select id
        from tests
        where topic_id = :topicId
        """, nativeQuery = true)
    List<Long> findIdsByTopicId(@Param("topicId") Long topicId);

    @Query(value = """
        select id
        from tests
        where learning_track_id = :learningTrackId
        """, nativeQuery = true)
    List<Long> findIdsByLearningTrackId(@Param("learningTrackId") Long learningTrackId);

    List<TestEntity> findByIdIn(List<Long> ids);

}
