package plugin.prep.assessment.feature.tests.mapper;

import org.mapstruct.*;

import plugin.prep.assessment.feature.tests.dto.answers.*;
import plugin.prep.assessment.feature.tests.entity.*;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    @Mapping(source = "question.id", target = "questionId")
    AnswerCreateResponse toDto(AnswerEntity entity);

    @Mapping(source = "question.id", target = "questionId")
    AnswerGetResponse toGetDto(AnswerEntity entity);

}
