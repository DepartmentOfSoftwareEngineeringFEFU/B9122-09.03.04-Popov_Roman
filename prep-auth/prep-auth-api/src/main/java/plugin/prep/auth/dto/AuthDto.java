package plugin.prep.auth.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

import static lombok.AccessLevel.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Schema(description = "Ответ аутентификации")
public class AuthDto {

    @Schema(description = "Access токен")
    private String accessToken;

    @Schema(description = "Refresh токен")
    private String refreshToken;

}
