package plugin.prep.assessment.feature.material.dto.material;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
public class MaterialGetRequest {

    @NotNull
    private Long userId;

    @NotNull
    private String level;

    @NotNull
    private Long topicId;

    private String subtopic;

    @Schema(
        description = "Номер страницы (с 0)",
        example = "0",
        type = "integer"
    )
    @NotNull(message = "номер страницы не может быть пустым")
    @Min(value = 0, message = "номер страницы должен быть больше или равен 0")
    @Max(value = 10000, message = "номер страницы не может превышать 10000")
    private int pageNumber;

    @Schema(
        description = "Размер страницы",
        example = "10",
        type = "integer"
    )
    @NotNull(message = "размер страницы не может быть пустым")
    @Min(value = 1, message = "размер страницы должен быть больше 0")
    @Max(value = 1000, message = "размер страницы не может превышать 1000")
    private int pageSize;

}
