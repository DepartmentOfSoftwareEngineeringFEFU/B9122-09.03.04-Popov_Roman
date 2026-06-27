package plugin.prep.assessment;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.security.config.annotation.method.configuration.*;

@SpringBootApplication
@EnableMethodSecurity
public class PrepAssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrepAssessmentApplication.class, args);
    }

}
