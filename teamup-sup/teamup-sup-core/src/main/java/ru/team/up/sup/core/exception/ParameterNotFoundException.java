package ru.team.up.sup.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Alexey Tkachenko
 */

public class ParameterNotFoundException extends ResponseStatusException {

    public ParameterNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Параметр не найден. ID = " + id);
    }
}
