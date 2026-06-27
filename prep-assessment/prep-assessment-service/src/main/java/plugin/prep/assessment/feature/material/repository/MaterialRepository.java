package plugin.prep.assessment.feature.material.repository;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.material.enums.*;

@Repository
public interface MaterialRepository extends JpaRepository<MaterialEntity, Long>,
    JpaSpecificationExecutor<MaterialEntity> {

    Page<MaterialEntity> findByGradeAndTopic(MaterialLevelEnum level, TopicEntity topic, Pageable pageable);

    Page<MaterialEntity> findByGrade(MaterialLevelEnum level, Pageable pageable);

    boolean existsByTitle(String title);

    Page<MaterialEntity> findByGradeAndTopicAndSubtopic(
        MaterialLevelEnum level,
        TopicEntity topic,
        String subtopic,
        Pageable pageable
    );

    @Query(value = """
        select id
        from materials
        where topic_id = :topicId
        """, nativeQuery = true)
    List<Long> findIdsByTopicId(@Param("topicId") Long topicId);

}
