package plugin.prep.example_service.config;

import org.springframework.context.annotation.*;

import plugin.prep.auth.*;

@Configuration
public class AppConfig {

    @Bean
    public AllowedUrls allowedUrls() {
        return () -> new String[]{"/open"};
    }

}
