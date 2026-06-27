package plugin.prep.auth;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.openfeign.*;

@EnableFeignClients
@SpringBootApplication
public class PrepAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrepAuthApplication.class, args);
    }

}
