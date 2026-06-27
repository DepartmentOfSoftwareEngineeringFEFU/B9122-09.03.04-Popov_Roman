package plugin.prep.auth.exceptions;

import org.springframework.http.*;

public class UserAlreadyExistsException extends AuthAppException {

    public UserAlreadyExistsException() {
        super("Пользователь с таким логином или почтой уже существует", HttpStatus.CONFLICT);
    }

}
