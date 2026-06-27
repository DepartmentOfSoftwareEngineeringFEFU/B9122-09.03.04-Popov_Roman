package plugin.prep.auth;

import java.time.*;
import java.util.*;

import com.auth0.jwt.*;
import lombok.*;

@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties props;

    private final JwtContext context;

    public String generateAccess(UUID jti, Principal principal) {
        return generateAccess(jti, principal, ZonedDateTime.now());
    }

    String generateAccess(UUID jti, Principal principal, ZonedDateTime now) {
        var jwt = JWT.create()
            .withJWTId(jti.toString())
            .withClaim("user_id", principal.getId())
            .withClaim("login", principal.getLogin())
            .withClaim("roles", principal.getRoles())
            .withIssuedAt(now.toInstant())
            .withExpiresAt(expireAccess(now))
            .sign(context.getAlgorithm());

        return jwt;
    }

    public String generateRefresh(UUID jti, Principal principal) {
        return generateRefresh(jti, principal, ZonedDateTime.now());
    }

    String generateRefresh(UUID jti, Principal principal, ZonedDateTime now) {
        var jwt = JWT.create()
            .withJWTId(jti.toString())
            .withClaim("user_id", principal.getId())
            .withClaim("login", principal.getLogin())
            .withIssuedAt(now.toInstant())
            .withExpiresAt(expireRefresh(now))
            .sign(context.getAlgorithm());

        return jwt;
    }

    private Instant expireAccess(ZonedDateTime time) {
        return expire(time, props.getAccessTokenExpiredOnSeconds());
    }

    private Instant expireRefresh(ZonedDateTime time) {
        return expire(time, props.getRefreshTokenExpiredOnSeconds());
    }

    private Instant expire(ZonedDateTime time, int timeoutSeconds) {
        return time.plusSeconds(timeoutSeconds).toInstant();
    }

}
