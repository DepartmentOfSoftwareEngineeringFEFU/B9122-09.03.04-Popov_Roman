package plugin.prep.assessment.feature.tests.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import plugin.prep.assessment.feature.tests.entity.*;

public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

    List<AnswerEntity> findByQuestionId(Long id);

    boolean existsByQuestionIdAndText(Long questionId, String text);

    @Modifying
    @Query(value = """
        delete from answers
        where question_id = :questionId
        """, nativeQuery = true)
    void deleteByQuestionId(@Param("questionId") Long questionId);

    @Modifying
    @Query(value = """
        delete from answers a
        using questions q
        where a.question_id = q.id
          and q.test_id = :testId
        """, nativeQuery = true)
    void deleteByTestId(@Param("testId") Long testId);

}
