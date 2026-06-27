package plugin.prep.errors;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PrepErrorsApplicationTest {

    @Autowired(required = false)
    private PrepErrorHandler handler;

    @Autowired(required = false)
    private PrepExceptionMapper mapper;

    @Test
    public void starter_beans_present_test() {
        assertNotNull(handler, "PrepErrorHandler must be present");
        assertNotNull(mapper, "PrepExceptionMapper must be present");
    }

}