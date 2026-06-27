package plugin.prep.assessment.feature.tests.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.tests.entity.*;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

    List<QuestionEntity> findByTestId(Long test);

    @Query(value = """
        select id
        from questions
        where topic_id = :topicId
        """, nativeQuery = true)
    List<Long> findIdsByTopicId(@Param("topicId") Long topicId);

    @Modifying
    @Query(value = """
        delete from questions
        where test_id = :testId
        """, nativeQuery = true)
    void deleteByTestId(@Param("testId") Long testId);

}
