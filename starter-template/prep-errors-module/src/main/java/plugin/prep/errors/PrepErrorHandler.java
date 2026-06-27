package plugin.prep.errors;

import java.time.*;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class PrepErrorHandler extends ResponseEntityExceptionHandler {

    private final PrepExceptionMapper mapper;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException ex) {
        log.error("Unexpected exception: {}", ex.getMessage(), ex);
        var dto = new ErrorDto(
            "Ошибка сервера",
            Instant.now()
        );

        var resp = ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(dto);

        return resp;
    }

    @ExceptionHandler(PrepException.class)
    public ResponseEntity<ErrorDto> handlePrepException(PrepException ex) {
        log.error("Application exception: {}", ex.getMessage(), ex);
        var dto = new ErrorDto(
            ex.getMessage(),
            Instant.now()
        );

        var resp = ResponseEntity
            .status(ex.getStatus())
            .body(dto);

        return resp;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleRuntimeException(IllegalArgumentException ex) {
        return handlePrepException(mapper.map(ex));
    }

}
