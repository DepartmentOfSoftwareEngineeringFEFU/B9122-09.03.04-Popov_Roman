package plugin.prep.auth.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.auth.dto.*;

@Tag(name = "Пользователи")
public interface UsersApi {

    @GetMapping("/users/me")
    @Operation(summary = "Получение информации о текущем пользователе")
    UserDto me();

    @PatchMapping("/users/grade")
    @Operation(summary = "Изменения уровня пользователя")
    UserDto changeGrade(@RequestBody UserChangeGradeDto request);

}
