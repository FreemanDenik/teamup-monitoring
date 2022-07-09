package ru.team.up.sup.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.team.up.sup.auth.exception.IncorrectDataRegistrationException;
import ru.team.up.sup.auth.service.UserServiceAuth;
import ru.team.up.sup.core.entity.Role;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.repositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class UserServiceAuthImpl implements UserServiceAuth {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceAuthImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User user) throws IncorrectDataRegistrationException {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10)));
        user.setRole(Role.ROLE_USER);
        user.setLastAccountActivity(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public boolean checkLogin(String login) {
        return  userRepository.findByEmail(login) != null;
    }
}