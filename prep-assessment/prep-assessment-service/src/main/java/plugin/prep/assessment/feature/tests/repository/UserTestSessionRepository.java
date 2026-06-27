package plugin.prep.assessment.feature.tests.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.tests.entity.*;

@Repository
public interface UserTestSessionRepository extends JpaRepository<UserTestSessionEntity, Long> {

    List<UserTestSessionEntity> findByUserIdAndTestIdAndIsActiveIsTrue(Long userId, Long testId);

    @Query(value = """
        select t.learning_track_id
        from user_test_sessions uts
        join tests t on t.id = uts.test_id
        where uts.user_id = :userId
          and uts.is_completed = true
          and t.learning_track_id is not null
        order by uts.end_at desc nulls last, uts.id desc
        limit 1
        """, nativeQuery = true)
    Optional<Long> findLatestCompletedLearningTrackIdByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = """
        delete from user_test_sessions
        where test_id = :testId
        """, nativeQuery = true)
    void deleteByTestId(@Param("testId") Long testId);

}
