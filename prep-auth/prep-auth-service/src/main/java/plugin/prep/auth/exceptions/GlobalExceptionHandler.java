package plugin.prep.auth.exceptions;

import static java.util.stream.Collectors.*;

import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

import plugin.prep.auth.dto.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_SERVER =
        "Что-то пошло не так. Внутренняя ошибка сервера";

    private static final String ERROR_VALIDATION_NULL =
        "Ошибка запроса, отсутствуют следующие параметры запроса:";

    private static final String ERROR_VALIDATION_PATTERN =
        "Ошибка запроса, некорректные данные в следующих параметрах:";

    private static final String ERROR_VALIDATION =
        "Ошибка запроса:";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException ex) {
        log.error("Unexpected exception", ex);
        var dto = new ErrorDto(ERROR_SERVER);

        var resp = ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(dto);

        return resp;
    }

    @ExceptionHandler(AuthAppException.class)
    public ResponseEntity<ErrorDto> handleDefaultException(AuthAppException ex) {
        log.error("Unexpected exception", ex);
        var dto = new ErrorDto(ex.getMessage());

        var resp = ResponseEntity
            .status(ex.getHttpStatus())
            .body(dto);

        return resp;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(
        MethodArgumentNotValidException ex
    ) {
        log.error("Unexpected exception", ex);
        var message = getValidationErrorMessage(ex.getBindingResult());
        var dto = new ErrorDto(message);

        var resp = ResponseEntity
            .status(BAD_REQUEST)
            .body(dto);

        return resp;
    }

    private String getValidationErrorMessage(BindingResult bindingResult) {
        var message = new StringBuilder();
        var errors = bindingResult.getFieldErrors();

        var nullFields = errors.stream()
            .filter(e -> "NotNull".equals(e.getCode()) || "NotBlank".equals(e.getCode()))
            .toList();

        if (!nullFields.isEmpty()) {
            var badFields = nullFields.stream()
                .map(FieldError::getField)
                .collect(joining(", "));

            message.append(ERROR_VALIDATION_NULL);
            message.append(badFields);
            return message.toString();
        }

        var invalidFields = errors.stream()
            .filter(e -> "Pattern".equals(e.getCode()))
            .toList();

        if (!invalidFields.isEmpty()) {
            var badFields = invalidFields.stream()
                .map(f -> f.getField() + " - " + f.getRejectedValue())
                .collect(joining(", "));

            message.append(ERROR_VALIDATION_PATTERN);
            message.append(badFields);
            return message.toString();
        }

        log.warn("No errors for validation exception");
        var badFields = errors.stream()
            .map(f -> f.getField() + " - " + f.getDefaultMessage())
            .collect(joining(", "));

        message.append(ERROR_VALIDATION);
        message.append(badFields);
        return message.toString();
    }

}
