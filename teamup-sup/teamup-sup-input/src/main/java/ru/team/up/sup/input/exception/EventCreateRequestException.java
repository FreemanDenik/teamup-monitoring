package ru.team.up.sup.input.exception;

/**
 * @author Pavel Kondrashov on 23.10.2021
 */
public class EventCreateRequestException extends RuntimeException {
    public EventCreateRequestException(String message) {
        super(message);
    }
}
