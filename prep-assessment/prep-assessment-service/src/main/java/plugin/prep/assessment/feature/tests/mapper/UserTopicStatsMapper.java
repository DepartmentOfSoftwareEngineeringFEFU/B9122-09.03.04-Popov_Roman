package plugin.prep.assessment.feature.tests.mapper;

import java.math.*;

import org.mapstruct.*;

import plugin.prep.assessment.feature.tests.dto.userTopicStats.*;
import plugin.prep.assessment.feature.tests.entity.*;
import plugin.prep.assessment.feature.tests.model.*;

@Mapper(componentModel = "spring")
public interface UserTopicStatsMapper {

    UserTopicStatsGetRequestModel toModel(UserTopicStatsGetRequest request);

    @Mapping(target = "accuracy", source = "accuracy", qualifiedByName = "bigDecimalToDouble")
    @Mapping(source = "topic.title", target = "topicTitle")
    SubtopicStatisticsResponse toGetDto(UserTopicStatsEntity entity);

    @Mapping(source = "topic.id", target = "topicId")
    UserTopicStatsGetAllResponse toGetAllDto(UserTopicStatsEntity entity);

    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        var res = value.doubleValue() * 100;
        return res;
    }

}
