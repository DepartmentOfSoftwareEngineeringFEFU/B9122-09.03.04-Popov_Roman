package plugin.prep.auth;

import lombok.extern.slf4j.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;

import org.junit.jupiter.api.*;

import static plugin.prep.auth.PostgresInstanceRunner.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class AbstractIT {

    private static PostgresInstanceRunner postgresRunner = of("postgres:14.17");

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        log.info("Configure properties");
        postgresRunner.configureProperties(registry);
    }

    @BeforeAll
    public static void beforeAll() {
        log.info("Start containers");
        postgresRunner.start();
    }

    @AfterAll
    public static void afterAll() {
        log.info("Keep containers for cached Spring test context");
    }

}
