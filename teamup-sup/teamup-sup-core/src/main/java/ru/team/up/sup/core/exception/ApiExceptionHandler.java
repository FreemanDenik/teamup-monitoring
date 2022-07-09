package ru.team.up.sup.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.constraints.NotNull;

/**
 * @author Alexey Tkachenko
 */

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(@NotNull Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler({NoContentException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String handleNoContent(@NotNull Exception e) {
        return e.getMessage();
    }
}
