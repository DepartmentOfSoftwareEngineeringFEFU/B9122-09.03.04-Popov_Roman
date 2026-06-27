package plugin.prep.auth.config;

import lombok.*;
import org.springframework.boot.context.properties.*;

@Getter
@Setter
@ConfigurationProperties(prefix = "prep.bootstrap.admin")
public class AdminBootstrapProperties {

    private boolean enabled;

    private String login;

    private String password;

    private String email;
}

