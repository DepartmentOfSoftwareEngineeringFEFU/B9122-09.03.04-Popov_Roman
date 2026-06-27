package plugin.prep.assessment.feature.tests.controller;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.assessment.feature.tests.api.*;
import plugin.prep.assessment.feature.tests.dto.userTestSession.*;
import plugin.prep.assessment.feature.tests.mapper.*;
import plugin.prep.assessment.feature.tests.service.*;

@RestController
@RequiredArgsConstructor
public class UserTestSessionController implements UserTestSessionApi {

    private final UserTestSessionService userTestSessionService;

    private final UserTestSessionMapper userTestSessionMapper;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserTestSessionCreateResponse create(UserTestSessionCreateRequest request) {

        var userTestSession = userTestSessionService.create(
            request.getUserId(),
            request.getTestId()
        );

        var response = userTestSessionMapper.toDto(userTestSession);
        return response;
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserTestSessionCompleteResponse setSessionComplete(UserTestSessionCompleteRequest request) {
        var userTestSessionComplete = userTestSessionService.setComplete(
            request.getId(),
            request.getIsComplete()
        );

        var response = userTestSessionMapper.toDto(userTestSessionComplete);
        return response;
    }

}
