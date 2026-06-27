package plugin.prep.assessment.feature.material.mapper;

import org.mapstruct.Mapper;
import plugin.prep.assessment.feature.material.dto.topic.TopicResponse;
import plugin.prep.assessment.feature.material.entity.TopicEntity;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicResponse toDto(TopicEntity entity);

}
