package plugin.prep.auth.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

import static lombok.AccessLevel.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Schema(description = "Ответ от ошибке")
public class ErrorDto {

    @Schema(description = "Сообщение от ошибке")
    private String message;

}
