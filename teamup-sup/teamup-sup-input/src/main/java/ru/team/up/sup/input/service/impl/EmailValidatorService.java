package ru.team.up.sup.input.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.team.up.sup.input.service.Validator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@Service("emailValidator")
@Slf4j
public class EmailValidatorService implements Validator {
    @Override
    public boolean validate(String email) {
        try {
            log.debug("Validate email: {}", email);
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            log.error("Email:{} is not valid", email);
            return false;
        }
        return true;
    }

    @Override
    public String uniformFormat(String value) {
        validate(value);
        return value.toLowerCase();
    }
}
