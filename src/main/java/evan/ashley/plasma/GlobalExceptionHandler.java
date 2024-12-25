package evan.ashley.plasma;

import evan.ashley.plasma.model.api.ResourceNotFoundException;
import evan.ashley.plasma.model.api.UnauthorizedException;
import evan.ashley.plasma.model.api.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(final Exception e) {
        final HttpStatus httpStatus;
        if (e instanceof ResourceNotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (e instanceof ValidationException) {
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (e instanceof UnauthorizedException) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else {
            log.error("A controller threw an irrecoverable exception.", e);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseStatusException(httpStatus, e.getMessage(), e);
    }
}
