package plugin.prep.errors;

import java.util.*;

import static org.springframework.http.HttpStatus.*;

import org.junit.jupiter.api.*;

public class PrepExceptionTest {

    @Test
    public void init_ex_showcase() {
        Assertions.assertThrows(
            PrepException.class, () -> {
                throw new PrepException(
                    String.format(
                        "Missing fields: %s",
                        String.join(",", List.of("abc"))
                    ), BAD_REQUEST);
            });
    }

    @Test
    public void init_ex_with_cause_showcase() {
        Assertions.assertThrows(
            PrepException.class, () -> {
                var cause = new RuntimeException(
                    "Bad thing happen!!!"
                );
                var ex = new PrepException(
                    String.format(
                        "Missing fields: %s",
                        String.join(",", List.of("abc"))
                    ), BAD_REQUEST, cause);
                throw ex;
            });
    }

}
