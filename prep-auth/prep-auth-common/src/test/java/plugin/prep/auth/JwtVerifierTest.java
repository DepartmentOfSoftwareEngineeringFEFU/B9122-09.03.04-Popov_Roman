package plugin.prep.auth;

import java.time.*;
import java.util.*;

import lombok.extern.slf4j.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import static plugin.prep.auth.IoUtils.*;

@Slf4j
public class JwtVerifierTest {

    private JwtProperties props = JwtProperties.builder()
        .privateKey(readResource("private.key"))
        .publicKey(readResource("public.key"))
        .accessTokenExpiredOnSeconds(360)
        .refreshTokenExpiredOnSeconds(604800)
        .build();

    private JwtContext ctx = new JwtContext(props);

    private JwtProvider provider = new JwtProvider(props, ctx);

    private JwtVerifier verifier = new JwtVerifier(ctx);

    @Test
    public void generate_actual_token() {
        var jti = UUID.randomUUID();
        var now = ZonedDateTime.now();

        var value = generateToken(jti, now);

        var token = verifier.verify(value);

        assertNotNull(token);
        assertEquals(jti.toString(), token.getId());
    }

    @Test
    public void generate_expired_token() {
        var jti = UUID.randomUUID();
        var now = ZonedDateTime.parse("2024-04-20T10:15:30+10:00");

        var value = generateToken(jti, now);

        assertThrows(
            IllegalStateException.class,
            () -> verifier.verify(value));
    }

    private String generateToken(UUID jti, ZonedDateTime dateTime) {
        var principal = Principal.builder()
            .id(123)
            .login("user")
            .roles(List.of("USER"))
            .build();

        var token = provider.generateAccess(jti, principal, dateTime);

        return token;
    }

}
