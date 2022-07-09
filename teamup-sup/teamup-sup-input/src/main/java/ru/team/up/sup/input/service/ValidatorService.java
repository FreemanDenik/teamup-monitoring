package ru.team.up.sup.input.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor(onConstructor = @__(@Autowired()))
public class ValidatorService {

    private final Validator emailValidator;

    public boolean validateEmail(String email) {
        return emailValidator.validate(email);
    }
}
