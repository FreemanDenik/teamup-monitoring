package ru.team.up.sup.input.serviceTest;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.team.up.sup.input.service.ValidatorService;
import ru.team.up.sup.input.service.impl.EmailValidatorService;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ValidatorsTest {

    private final ValidatorService validator =
            new ValidatorService(new EmailValidatorService());

    private final String[] validEmailIds = new String[]{"journaldev@yahoo.com", "journaldev-100@yahoo.com",
            "journaldev.100@yahoo.com", "journaldev111@journaldev.com", "journaldev-100@journaldev.net",
            "journaldev.100@journaldev.com.au", "journaldev@1.com", "journaldev@gmail.com.com",
            "journaldev+100@gmail.com", "journaldev-100@yahoo-test.com", "journaldev_100@yahoo-test.ABC.CoM"};

    private final String[] invalidEmailIds = new String[]{"journaldev", "journaldev@.com.my",
            "journaldev123@.com", "journaldev123@.com.com",
            "journaldev()*@gmail.com", "journaldev@%*.com",
            "journaldev@journaldev@gmail.com"};

    @Test
    public void validEmail() {
        for (String temp : validEmailIds) {
            assertTrue(validator.validateEmail(temp));
        }
        for (String temp : invalidEmailIds) {
            assertFalse(validator.validateEmail(temp));
        }
    }
}