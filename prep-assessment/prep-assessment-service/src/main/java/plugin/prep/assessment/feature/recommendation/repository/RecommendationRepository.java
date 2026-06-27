package plugin.prep.assessment.feature.recommendation.repository;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.recommendation.entity.*;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendationEntity, Long> {

    List<RecommendationEntity> findAllByUserId(Long userId, Limit limit);

    void deleteAllByUserId(Long userId);

    @Query(value = """
        select id
        from recommendations
        where topic_id = :topicId
        """, nativeQuery = true)
    List<Long> findIdsByTopicId(@Param("topicId") Long topicId);

}
