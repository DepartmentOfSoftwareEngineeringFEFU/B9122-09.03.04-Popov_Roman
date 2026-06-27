package plugin.prep.auth.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import plugin.prep.auth.entity.*;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

}
