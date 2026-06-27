package plugin.prep.assessment.feature.material.mapper;

import org.mapstruct.*;

import plugin.prep.assessment.feature.material.dto.material.*;
import plugin.prep.assessment.feature.material.entity.*;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

    @Mapping(source = "file.id", target = "fileId")
    MaterialGetResponse toGetDto(MaterialEntity entity);

    @Mapping(source = "topic.id", target = "id")
    @Mapping(source = "topic.title", target = "topicTitle")
    MaterialTopicGetResponse toGetTopicDto(MaterialEntity entity);

}
