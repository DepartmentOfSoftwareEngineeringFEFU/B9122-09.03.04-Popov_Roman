package plugin.prep.auth.exceptions;

import org.springframework.http.*;

import org.junit.*;
import org.junit.jupiter.api.Test;

public class AuthAppExceptionTest {

    @Test
    public void create_new_one() {
        Assert.assertThrows(AuthAppException.class, this::throwSomeAppEx);
    }

    @Test
    public void wrap_internal_exception() {
        Assert.assertThrows(RuntimeException.class, this::throwSomeRuntimeEx);
    }

    public void throwSomeAppEx() {
        var cond = false;
        if (!cond) {
            throw new AuthAppException(
                String.format(
                    "Something goes wrong: %s", cond
                ), HttpStatus.ACCEPTED);
        }
    }

    public void throwSomeRuntimeEx() {
        try {
            throw new RuntimeException("Some internal error");
        } catch (RuntimeException ex) {
            throw new AuthAppException(
                "Something goes wrong",
                HttpStatus.ACCEPTED,
                ex);
        }
    }

}
