package plugin.prep.errors;

import lombok.*;
import org.springframework.http.*;

@AllArgsConstructor
public class Exceptions {

    public static PrepException badRequest(String message) {
        return new PrepException(message, HttpStatus.BAD_REQUEST);
    }

    public static PrepException notFound(String message) {
        return new PrepException(message, HttpStatus.NOT_FOUND);
    }

    public static PrepException conflict(String message) {
        return new PrepException(message, HttpStatus.CONFLICT);
    }

    public static PrepException forbidden(String message) {
        return new PrepException(message, HttpStatus.FORBIDDEN);
    }

    public static PrepException unauthorized(String message) {
        return new PrepException(message, HttpStatus.UNAUTHORIZED);
    }

    public static PrepException internalError(String message) {
        return new PrepException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
