package plugin.prep.assessment.feature.material.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.material.entity.*;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Long> {

    boolean existsByTitle(String title);

    List<TopicEntity> findByIdIn(List<Long> ids);

}
