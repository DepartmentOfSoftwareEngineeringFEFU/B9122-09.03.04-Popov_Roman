package plugin.prep.assessment.feature.readiness.api;

import io.swagger.v3.oas.annotations.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.readiness.dto.*;

public interface ReadinessApi {

    @GetMapping("/readiness/me")
    @Operation(description = "Получение актуального показателя готовности текущего пользователя")
    ReadinessIndicatorResponse getCurrent();

    @PostMapping("/readiness/recalculate")
    @Operation(description = "Принудительный пересчет показателя готовности текущего пользователя")
    ReadinessIndicatorResponse recalculate();

}
