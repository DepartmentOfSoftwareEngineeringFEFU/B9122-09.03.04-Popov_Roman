package plugin.prep.assessment.feature.readiness.service;

import lombok.*;
import org.springframework.stereotype.*;

import plugin.prep.auth.*;
import plugin.prep.errors.*;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final JwtAuthHolder jwtAuthHolder;

    public Long getCurrentUserId() {
        var authentication = jwtAuthHolder.get();

        if (authentication instanceof JwtAuth jwtAuth && jwtAuth.getUserId() != null) {
            return jwtAuth.getUserId();
        }

        throw Exceptions.badRequest("Не удалось определить текущего пользователя");
    }

}
