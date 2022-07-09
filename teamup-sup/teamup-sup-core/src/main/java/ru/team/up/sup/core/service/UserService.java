package ru.team.up.sup.core.service;

import ru.team.up.sup.core.entity.User;

import java.util.List;

/**
 * @author Alexey Tkachenko
 */
public interface UserService {
    List<User> getAllUsers();

    User getOneUser(Long id);

    User saveUser(User user);

    void deleteUser(Long id);

    User getUserByName(String name);

}
