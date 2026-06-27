package plugin.prep.auth.repository;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import plugin.prep.auth.*;
import plugin.prep.auth.entity.*;

@Slf4j
@Transactional
public class RolesRepositoryIT extends AbstractIT {

    @Autowired
    private RolesRepository repository;

    @Test
    public void save_role_success() {
        var role = Role.builder()
            .name("Модератор")
            .code("MODERATOR")
            .build();
        var saved = repository.save(role);

        log.info("saved role: {}", saved);
        assertTrue(saved.getId() > 0);

        var existed = repository
            .findById(saved.getId())
            .orElseThrow();

        assertEquals(saved.getId(), existed.getId());
        assertEquals("Модератор", existed.getName());
        assertEquals("MODERATOR", existed.getCode());
    }

    @Test
    public void paged_request_example() {
        var sort = Sort.by("id");
        var page = PageRequest.of(0, 10, sort);

        var roles = repository.findAll(page);
        log.info("roles from db: {}", roles);
        log.info(
            "counts from db: {},{},{}",
            roles.getNumberOfElements(),
            roles.getTotalElements(),
            roles.getTotalPages());
    }

}
