package plugin.prep.auth.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.auth.dto.*;

@Tag(name = "Авторизация")
public interface AuthApi {

    @PostMapping("/login")
    @Operation(summary = "Аутентификация пользователя")
    AuthDto login(@Valid @RequestBody LoginRequest request);

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    AuthDto register(@Valid @RequestBody RegisterRequest request);

    @PostMapping("/refresh")
    @Operation(summary = "Обновление пары JWT токенов")
    AuthDto refresh(@Valid @RequestBody TokenRequest request);

    @PostMapping("/logout")
    @Operation(summary = "Удаление активных сессий")
    void logout(@Valid @RequestBody TokenRequest request);

}
