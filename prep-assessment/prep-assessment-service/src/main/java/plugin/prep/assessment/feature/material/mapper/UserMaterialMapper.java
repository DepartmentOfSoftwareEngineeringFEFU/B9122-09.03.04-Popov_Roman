package plugin.prep.assessment.feature.material.mapper;

import org.mapstruct.*;

import plugin.prep.assessment.feature.material.dto.material.*;
import plugin.prep.assessment.feature.material.entity.*;

@Mapper(componentModel = "spring")
public interface UserMaterialMapper {

    MaterialSetLikeResponse toDto(UserMaterialEntity entity);

}
