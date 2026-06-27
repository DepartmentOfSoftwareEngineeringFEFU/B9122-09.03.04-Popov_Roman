package plugin.prep.assessment.feature.tests.mapper;

import org.mapstruct.*;

import plugin.prep.assessment.feature.tests.dto.userTestSession.*;
import plugin.prep.assessment.feature.tests.entity.*;
import plugin.prep.assessment.feature.tests.model.*;

@Mapper(componentModel = "spring")
public interface UserTestSessionMapper {

    @Mapping(source = "test.id", target = "testId")
    UserTestSessionCreateResponseModel toModel(UserTestSessionEntity entity);

    UserTestSessionCreateResponse toDto(UserTestSessionCreateResponseModel model);

    UserTestSessionCompleteResponse toDto(UserTestSessionCompleteResponseModel model);

}
