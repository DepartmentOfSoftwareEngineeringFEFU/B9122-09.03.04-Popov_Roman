package plugin.prep.assessment.feature.tests.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.dto.userTestSession.*;

@Tag(name = "User Test Session Api", description = "Тестовая сессия пользователя")
public interface UserTestSessionApi {

    @PostMapping("/user-test-sessions")
    @Operation(summary = "Создание сессии пользователя")
    UserTestSessionCreateResponse create(@RequestBody UserTestSessionCreateRequest request);

    @PostMapping("/user-test-sessions/complete")
    @Operation(summary = "Завершение сессии пользователя")
    UserTestSessionCompleteResponse setSessionComplete(@RequestBody UserTestSessionCompleteRequest request);

}
