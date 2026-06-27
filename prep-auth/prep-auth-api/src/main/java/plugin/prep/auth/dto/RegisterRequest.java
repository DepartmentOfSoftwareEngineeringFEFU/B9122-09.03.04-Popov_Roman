package plugin.prep.auth.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;

import static lombok.AccessLevel.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Schema(description = "Запрос регистрации пользователя")
public class RegisterRequest {

    @NotBlank
    @Size(max = 255)
    @Email
    @Schema(description = "Почта пользователя")
    private String email;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Логин пользователя")
    private String login;

    @NotBlank
    @Size(min = 8, max = 255)
    @Schema(description = "Пароль пользователя")
    private String password;

    @NotNull
    @Schema(description = "Идентификатор направления подготовки")
    private Long learningTrackId;

}
