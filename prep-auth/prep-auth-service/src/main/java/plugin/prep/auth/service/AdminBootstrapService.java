package plugin.prep.auth.service;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.boot.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import plugin.prep.auth.config.*;
import plugin.prep.auth.entity.*;
import plugin.prep.auth.repository.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminBootstrapService implements ApplicationRunner {

    private static final String ADMIN_ROLE_CODE = "ROLE_ADMIN";

    private final AdminBootstrapProperties properties;

    private final UsersRepository usersRepository;

    private final RolesRepository rolesRepository;

    private final CredentialsRepository credentialsRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!properties.isEnabled()) {
            return;
        }

        var login = trim(properties.getLogin());
        var password = trim(properties.getPassword());
        var email = trim(properties.getEmail());

        if (login == null || password == null || email == null) {
            log.warn("Admin bootstrap is enabled, but login, password or email is empty. Skipping seed.");
            return;
        }

        var adminRole = rolesRepository.findByCode(ADMIN_ROLE_CODE);
        if (adminRole == null) {
            throw new IllegalStateException("ROLE_ADMIN was not found");
        }

        var existingUser = usersRepository.findByLogin(login);
        if (existingUser != null) {
            ensureAdmin(existingUser, adminRole, password);
            return;
        }

        if (usersRepository.existsByEmail(email)) {
            log.warn("Admin bootstrap skipped: email '{}' is already used by another user.", email);
            return;
        }

        var user = User.builder()
            .login(login)
            .email(email)
            .role(adminRole)
            .build();
        var savedUser = usersRepository.save(user);
        createCredential(savedUser, password);

        log.info("Bootstrap admin '{}' has been created.", login);
    }

    private void ensureAdmin(User user, Role adminRole, String password) {
        if (!ADMIN_ROLE_CODE.equals(user.getRole().getCode())) {
            user.setRole(adminRole);
            usersRepository.save(user);
            log.info("Admin bootstrap granted ROLE_ADMIN to existing user '{}'.", user.getLogin());
        } else {
            log.info("Admin bootstrap skipped: user '{}' already has ROLE_ADMIN.", user.getLogin());
        }

        if (credentialsRepository.findByUserId(user.getId()) == null) {
            createCredential(user, password);
            log.info("Admin bootstrap created credentials for existing user '{}'.", user.getLogin());
        }
    }

    private void createCredential(User user, String password) {
        var salt = BCrypt.gensalt();
        var credential = Credential.builder()
            .user(user)
            .salt(salt)
            .hash(BCrypt.hashpw(password, salt))
            .build();
        credentialsRepository.save(credential);
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }

        var trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

