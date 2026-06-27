package plugin.prep.assessment.feature.tests.mapper;

import org.mapstruct.*;

import plugin.prep.assessment.feature.tests.dto.question.*;
import plugin.prep.assessment.feature.tests.entity.*;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(source = "test.id", target = "testId")
    QuestionResponse toDto(QuestionEntity entity);

}
