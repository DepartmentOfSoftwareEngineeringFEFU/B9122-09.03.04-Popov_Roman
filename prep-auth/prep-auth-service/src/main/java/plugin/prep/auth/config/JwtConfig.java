package plugin.prep.auth.config;

import org.springframework.context.annotation.*;

import plugin.prep.auth.*;

@Configuration
public class JwtConfig {

    @Bean
    public AllowedUrls allowedUrls() {
        var allowedUrls = new String[]{
            "/login",
            "/register",
            "/refresh",
            "/logout",
            "/swagger-ui/**",
            "/v3/**"
        };
        return () -> allowedUrls;
    }

}
