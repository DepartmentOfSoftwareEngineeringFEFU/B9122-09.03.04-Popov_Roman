package plugin.prep.errors;

import java.time.*;

import lombok.*;

@Data
public class ErrorDto {

    private final String message;

    private final Instant time;

}
