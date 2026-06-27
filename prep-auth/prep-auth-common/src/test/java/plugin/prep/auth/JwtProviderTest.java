package plugin.prep.auth;

import java.security.*;
import java.time.*;
import java.util.*;

import com.auth0.jwt.*;
import lombok.extern.slf4j.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import static plugin.prep.auth.IoUtils.*;

@Slf4j
class JwtProviderTest {

    private JwtProperties props = JwtProperties.builder()
        .privateKey(readResource("private.key"))
        .publicKey(readResource("public.key"))
        .accessTokenExpiredOnSeconds(360)
        .refreshTokenExpiredOnSeconds(604800)
        .build();

    private JwtContext ctx = new JwtContext(props);

    private JwtProvider provider = new JwtProvider(props, ctx);

    @Test
    @Disabled
    public void generate_keys() throws Exception {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        var keyBytes = pair.getPrivate().getEncoded();
        var keyBase64 = Base64.getEncoder().encode(keyBytes);
        var keyValue = new String(keyBase64);
        log.info("Key: {}", keyValue);
        writeResource("private.key", keyBase64);

        var pubBytes = pair.getPublic().getEncoded();
        var pubBase64 = Base64.getEncoder().encode(pubBytes);
        var pubValue = new String(pubBase64);
        writeResource("public.key", pubBase64);
        log.info("Pub: {}", pubValue);
    }

    @Test
    public void generate_access_token() {
        var jti = UUID.fromString("0cfcc90c-5200-41cb-ab17-5338556fa072");
        var now = ZonedDateTime.parse("2025-04-20T10:15:30+10:00");
        var principal = Principal.builder()
            .id(123)
            .login("user")
            .roles(List.of("USER"))
            .build();

        var token = provider.generateAccess(jti, principal, now);
        log.info("Access token: {}", token);

        var decoded = JWT.decode(token);
        assertEquals(jti.toString(), decoded.getId());
        assertEquals(123, decoded.getClaim("user_id").asLong());
        assertEquals("user", decoded.getClaim("login").asString());
        assertEquals(List.of("USER"), decoded.getClaim("roles").asList(String.class));
    }

    @Test
    public void generate_refresh_token() {
        var jti = UUID.fromString("0cfcc90c-5200-41cb-ab17-5338556fa072");
        var now = ZonedDateTime.parse("2025-04-20T10:15:30+10:00");

        var principal = Principal.builder()
            .id(123)
            .login("user")
            .roles(List.of("USER"))
            .build();

        var token = provider.generateRefresh(jti, principal, now);
        log.info("Refresh token: {}", token);

        var decoded = JWT.decode(token);
        assertEquals(jti.toString(), decoded.getId());
        assertEquals(123, decoded.getClaim("user_id").asLong());
        assertEquals("user", decoded.getClaim("login").asString());
    }

}
