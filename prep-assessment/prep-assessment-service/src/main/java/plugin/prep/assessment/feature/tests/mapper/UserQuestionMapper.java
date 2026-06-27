package plugin.prep.assessment.feature.tests.mapper;

import org.mapstruct.*;

import plugin.prep.assessment.feature.tests.dto.userQuestion.*;
import plugin.prep.assessment.feature.tests.entity.*;

@Mapper(componentModel = "spring")
public interface UserQuestionMapper {

    @Mapping(source = "answer.id", target = "answerId")
    @Mapping(source = "question.id", target = "questionId")
    UserQuestionCreateResponse toDto(UserQuestionEntity entity);

    @Mapping(source = "answer.id", target = "answerId")
    @Mapping(source = "question.id", target = "questionId")
    UserQuestionGetResponse toGetDto(UserQuestionEntity entity);

}
