package plugin.prep.assessment.feature.tests.mapper;

import org.mapstruct.*;

import plugin.prep.assessment.feature.tests.dto.test.*;
import plugin.prep.assessment.feature.tests.entity.*;

@Mapper(componentModel = "spring")
public interface TestMapper {

    @Mapping(source = "topic.title", target = "topicTitle")
    TestResponse toDto(TestEntity entity);

}
