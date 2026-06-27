package plugin.prep.errors;

import java.time.*;

import lombok.extern.slf4j.*;

import org.junit.jupiter.api.*;

@Slf4j
public class ErrorDtoTest {

    @Test
    public void foo() {
        var error = new ErrorDto(
            "Some error",
            Instant.parse("2025-12-15T10:10:30.00Z")
        );
        log.error("Error: {}", error);
    }

}