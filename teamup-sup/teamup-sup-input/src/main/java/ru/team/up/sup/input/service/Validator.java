package ru.team.up.sup.input.service;

public interface Validator {
    boolean validate(String value);
    String uniformFormat(String value);
}
