package plugin.prep.assessment.feature.readiness.mapper;

import org.mapstruct.*;

import plugin.prep.assessment.feature.readiness.dto.*;
import plugin.prep.assessment.feature.readiness.entity.*;

@Mapper(componentModel = "spring")
public interface ReadinessMapper {

    ReadinessIndicatorResponse toDto(ReadinessIndicatorEntity entity);

}
