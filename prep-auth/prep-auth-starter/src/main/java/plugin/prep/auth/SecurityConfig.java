package plugin.prep.auth;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

/**
 * Только для примера, такую конфигурацию нужно завести в своем сервисе.
 * <br/>
 * Почему-то есть разница при импорте из стартера не работает как надо.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private final JwtAuthFilter jwtFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        AllowedUrls allowedUrls
    ) throws Exception {
        log.info("Loockup SecurityFilterChain");
        return http.cors(customizer -> {})
            .csrf(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .exceptionHandling(c -> c
                .authenticationEntryPoint(jwtAuthEntryPoint))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(c -> c
                .requestMatchers(allowedUrls.getAllowedUrls()).permitAll()
                .anyRequest().authenticated())
            .sessionManagement(c -> c
                .sessionCreationPolicy(STATELESS))
            .build();
    }

}
