package plugin.prep.assessment.feature.readiness.controller;

import lombok.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.readiness.api.*;
import plugin.prep.assessment.feature.readiness.dto.*;
import plugin.prep.assessment.feature.readiness.mapper.*;
import plugin.prep.assessment.feature.readiness.service.*;

@RestController
@RequiredArgsConstructor
public class ReadinessController implements ReadinessApi {

    private final ReadinessService readinessService;

    private final ReadinessMapper readinessMapper;

    private final CurrentUserService currentUserService;

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ReadinessIndicatorResponse getCurrent() {
        var userId = currentUserService.getCurrentUserId();
        var readiness = readinessService.getOrCalculate(userId);
        return readinessMapper.toDto(readiness);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ReadinessIndicatorResponse recalculate() {
        var userId = currentUserService.getCurrentUserId();
        var readiness = readinessService.calculateAndSave(userId);
        return readinessMapper.toDto(readiness);
    }

}
