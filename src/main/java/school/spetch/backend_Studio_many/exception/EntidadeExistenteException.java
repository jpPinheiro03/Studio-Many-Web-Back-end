package school.spetch.backend_Studio_many.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntidadeExistenteException extends RuntimeException {
    public EntidadeExistenteException(String message) {
        super(message);
    }
}
