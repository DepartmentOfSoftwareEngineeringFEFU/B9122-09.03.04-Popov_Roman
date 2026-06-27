package plugin.prep.assessment.feature.readiness.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import plugin.prep.assessment.feature.readiness.entity.*;

@Repository
public interface ReadinessIndicatorRepository extends JpaRepository<ReadinessIndicatorEntity, Long> {

    Optional<ReadinessIndicatorEntity> findByUserId(Long userId);

}
