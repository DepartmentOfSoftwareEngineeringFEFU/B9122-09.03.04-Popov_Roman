package plugin.prep.auth.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import plugin.prep.auth.entity.*;

public interface RolesRepository extends JpaRepository<Role, Long>, PagingAndSortingRepository<Role, Long> {

    Role findByCode(String code);

}
