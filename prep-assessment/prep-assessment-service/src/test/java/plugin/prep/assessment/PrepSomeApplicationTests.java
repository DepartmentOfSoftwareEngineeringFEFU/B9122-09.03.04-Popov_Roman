package plugin.prep.assessment;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import plugin.prep.errors.*;

@SpringBootTest
class PrepSomeApplicationTests {

    @Autowired(required = false)
    private PrepErrorHandler handler;

    @Test
    void contextLoads() {
        assertNotNull(handler, "PrfxErrorHandler must be present");
    }

}
