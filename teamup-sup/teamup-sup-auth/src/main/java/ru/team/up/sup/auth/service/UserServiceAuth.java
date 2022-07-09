package ru.team.up.sup.auth.service;

import ru.team.up.sup.core.entity.User;

public interface UserServiceAuth {
    void saveUser(User user);
    boolean checkLogin(String login);
}
