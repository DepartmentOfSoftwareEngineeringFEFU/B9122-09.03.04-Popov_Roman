package plugin.prep.auth.mapper;

import org.mapstruct.*;

import plugin.prep.auth.dto.*;
import plugin.prep.auth.entity.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.name", target = "role")
    UserDto toDto(User entity);

}
