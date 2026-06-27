package plugin.prep.auth;

import lombok.extern.slf4j.*;
import org.springframework.test.context.*;
import org.testcontainers.containers.*;

@Slf4j
public class PostgresInstanceRunner {

    private final PostgreSQLContainer<?> container;

    public PostgresInstanceRunner(PostgreSQLContainer<?> container) {
        this.container = container;
    }

    public static PostgresInstanceRunner of(String imageName) {
        var container = new PostgreSQLContainer<>(imageName);
        return new PostgresInstanceRunner(container);
    }

    public void configureProperties(DynamicPropertyRegistry registry) {
        start();
        if (container.isRunning()) {
            log.info("Register testcontainers PostgresSQL properties");
            registry.add("spring.datasource.username", container::getUsername);
            registry.add("spring.datasource.password", container::getPassword);
            registry.add("spring.datasource.url", container::getJdbcUrl);
        }
    }

    public void start() {
        if (!container.isRunning()) {
            log.info("Start testcontainers PostgresSQL");
            try {
                container.start();
            } catch (Exception ex) {
                log.warn("Container PostgresSQL didn't start");
            }
        }
    }

    public void stop() {
        log.info("Stop testcontainers PostgresSQL");
        if (container.isCreated()) {
            container.stop();
        }
    }

}
