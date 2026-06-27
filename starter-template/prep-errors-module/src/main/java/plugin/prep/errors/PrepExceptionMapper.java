package plugin.prep.errors;

import org.springframework.http.*;
import org.springframework.stereotype.*;

@Component
public class PrepExceptionMapper {

    public PrepException map(IllegalArgumentException cause) {
        return new PrepException(
            String.format(
                "Bad input: %s",
                cause.getMessage()
            ), HttpStatus.BAD_REQUEST, cause);
    }

}
