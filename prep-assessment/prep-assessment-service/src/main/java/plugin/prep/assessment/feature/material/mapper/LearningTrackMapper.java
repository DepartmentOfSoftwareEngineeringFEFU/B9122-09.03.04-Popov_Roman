package plugin.prep.assessment.feature.material.mapper;

import java.util.*;

import org.mapstruct.*;

import plugin.prep.assessment.feature.material.dto.learningTrack.*;
import plugin.prep.assessment.feature.material.entity.*;

@Mapper(componentModel = "spring")
public interface LearningTrackMapper {

    @Mapping(source = "topics", target = "topicIds")
    LearningTrackResponse toDto(LearningTracksEntity entity);

    default List<Long> topicsToTopicIds(List<TopicEntity> topics) {
        if (topics == null) {
            return List.of();
        }

        return topics.stream()
            .map(TopicEntity::getId)
            .toList();
    }

}
