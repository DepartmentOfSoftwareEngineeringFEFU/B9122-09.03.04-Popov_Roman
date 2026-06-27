package plugin.prep.auth.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

import static lombok.AccessLevel.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Schema(description = "Информация о пользователе")
public class UserDto {

    @Schema(description = "Идентификатор пользователя")
    private Long id;

    @Schema(description = "Почта пользователя")
    private String email;

    @Schema(description = "Логин пользователя")
    private String login;

    @Schema(description = "Уровень пользователя")
    private String grade;

    private Long learningTrackId;

    @Schema(description = "Роль пользователя")
    private String role;

}
