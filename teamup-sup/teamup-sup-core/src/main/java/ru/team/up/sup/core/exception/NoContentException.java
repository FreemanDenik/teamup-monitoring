package ru.team.up.sup.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


/**
 * @author Alexey Tkachenko
 */
public class NoContentException extends ResponseStatusException {
    public NoContentException() {
        super(HttpStatus.NO_CONTENT, "Список параметров пуст.");
    }
}
