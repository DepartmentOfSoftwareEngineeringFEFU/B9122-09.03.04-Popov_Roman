package plugin.prep.assessment.feature.tests.repository;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.material.entity.*;
import plugin.prep.assessment.feature.tests.entity.*;

@Repository
public interface UserTopicStatsRepository extends JpaRepository<UserTopicStatsEntity, Long> {

    List<UserTopicStatsEntity> findByUserId(Long userId, Limit limit);

    List<UserTopicStatsEntity> findByUserId(Long userId);

    List<UserTopicStatsEntity> findByUserIdAndTopic(Long userId, TopicEntity topic);

    UserTopicStatsEntity findByUserIdAndTopicAndSubtopic(Long userId, TopicEntity topic, String subtopic);

    @Query("""
        select uts
        from user_topic_stats uts
        where uts.userId = :userId
          and uts.topic.id in :topicIds
        """)
    List<UserTopicStatsEntity> findByUserIdAndTopicIds(
        @Param("userId") Long userId,
        @Param("topicIds") List<Long> topicIds
    );

    @Query(value = """
        select id
        from user_topic_stats
        where topic_id = :topicId
        """, nativeQuery = true)
    List<Long> findIdsByTopicId(@Param("topicId") Long topicId);

}
