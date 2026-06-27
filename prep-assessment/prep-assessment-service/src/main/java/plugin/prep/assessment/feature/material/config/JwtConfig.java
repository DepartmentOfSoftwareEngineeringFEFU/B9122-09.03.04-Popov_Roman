package plugin.prep.assessment.feature.material.config;

import org.springframework.context.annotation.*;

import plugin.prep.auth.*;

@Configuration
public class JwtConfig {

    @Bean
    public AllowedUrls allowedUrls() {
        var allowedUrls = new String[]{
            "/learning-tracks",
            "/swagger-ui/**",
            "/v3/**"
        };
        return () -> allowedUrls;
    }

}
