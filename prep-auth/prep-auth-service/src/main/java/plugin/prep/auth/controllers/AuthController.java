package plugin.prep.auth.controllers;

import lombok.*;
import org.springframework.web.bind.annotation.*;

import plugin.prep.auth.api.*;
import plugin.prep.auth.dto.*;
import plugin.prep.auth.service.*;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public AuthDto login(LoginRequest request) {
        return authService.login(request);
    }

    @Override
    public AuthDto register(RegisterRequest request) {
        return authService.register(request);
    }

    @Override
    public AuthDto refresh(TokenRequest request) {
        return authService.refresh(request);
    }

    @Override
    public void logout(TokenRequest request) {
        authService.logout(request);
    }

}
