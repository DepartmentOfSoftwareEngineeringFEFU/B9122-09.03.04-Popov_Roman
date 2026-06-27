package plugin.prep.assessment.feature.tests.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.tests.entity.*;

@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestionEntity, Long> {

    @Query(value = """
            select uq.* from user_questions uq
            join questions q on q.id = uq.question_id
            where q.test_id = :testId
        """, nativeQuery = true)
    List<UserQuestionEntity> findByTestId(@Param("testId") Long testId);

    @Query(value = """
            select distinct on (uq.question_id) uq.* from user_questions uq
            join questions q on q.id = uq.question_id
            where uq.user_id = :userId
              and q.test_id = :testId
            order by uq.question_id, uq.created_at desc, uq.id desc
        """, nativeQuery = true)
    List<UserQuestionEntity> findByUserIdAndTestId(
        @Param("userId") Long userId,
        @Param("testId") Long testId
    );

    @Modifying
    @Query(value = """
            delete from user_questions
            where question_id = :questionId
        """, nativeQuery = true)
    void deleteByQuestionId(@Param("questionId") Long questionId);

    @Modifying
    @Query(value = """
            delete from user_questions
            where answer_id = :answerId
        """, nativeQuery = true)
    void deleteByAnswerId(@Param("answerId") Long answerId);

    @Modifying
    @Query(value = """
            delete from user_questions uq
            using questions q
            where uq.question_id = q.id
              and q.test_id = :testId
        """, nativeQuery = true)
    void deleteByTestId(@Param("testId") Long testId);

}
