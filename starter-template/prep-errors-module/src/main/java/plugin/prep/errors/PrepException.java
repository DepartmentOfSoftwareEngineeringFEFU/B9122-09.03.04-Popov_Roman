package plugin.prep.errors;

import lombok.*;
import org.springframework.http.*;

@Getter
public class PrepException extends RuntimeException {

    private final HttpStatus status;

    public PrepException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public PrepException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

}
