package plugin.prep.errors;

import jakarta.annotation.*;
import lombok.extern.slf4j.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.web.servlet.*;
import org.springframework.context.annotation.*;

@Slf4j
@Configuration
@Import({PrepExceptionMapper.class, PrepErrorHandler.class})
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class PrepErrorsAutoConfiguration {

    @PostConstruct
    public void init() {
        log.debug("It's alive!!!");
    }

}
