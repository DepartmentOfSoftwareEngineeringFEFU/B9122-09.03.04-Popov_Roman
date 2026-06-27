package plugin.prep.auth.repository;

import org.springframework.data.jpa.repository.*;

import plugin.prep.auth.entity.*;

public interface CredentialsRepository extends JpaRepository<Credential, Long> {

    Credential findByUserId(Long userId);

}
