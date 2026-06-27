package plugin.prep.auth.entity;

import lombok.extern.slf4j.*;
import org.springframework.security.crypto.bcrypt.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CredentialTest {

    @Test
    public void hashing_test() {
        var pass = "hello";
        log.info("pass: {}", pass);

        var salt = BCrypt.gensalt();
        log.info("salt: {}", salt);

        var hash = BCrypt.hashpw(pass, salt);
        log.info("hash: {}", hash);

        var match = BCrypt.checkpw(pass, hash);
        assertTrue(match);
    }

}
