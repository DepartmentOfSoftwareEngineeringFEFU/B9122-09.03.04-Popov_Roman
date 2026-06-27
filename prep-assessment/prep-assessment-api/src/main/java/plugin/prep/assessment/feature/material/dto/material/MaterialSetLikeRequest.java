package plugin.prep.assessment.feature.material.dto.material;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true)
@Schema(description = "Запрос на добавить/убрать материала в избранное")
public class MaterialSetLikeRequest {

    @Schema(description = "id пользователя")
    private Long userId;

    @Schema(description = "id материала")
    private Long materialId;

    private Boolean isLiked;

}
