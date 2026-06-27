package plugin.prep.assessment.feature.recommendation.mapper;

import org.mapstruct.*;

import plugin.prep.assessment.feature.recommendation.dto.*;
import plugin.prep.assessment.feature.recommendation.entity.*;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "topic.title", target = "topicTitle")
    RecommendationCreateResponse toDto(RecommendationEntity entity);

}
