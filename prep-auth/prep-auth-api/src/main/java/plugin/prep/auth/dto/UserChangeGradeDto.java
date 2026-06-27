package plugin.prep.auth.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

import static lombok.AccessLevel.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Schema(description = "Запрос смены уровня пользователя")
public class UserChangeGradeDto {
    private Long userId;

    private String grade;
}
