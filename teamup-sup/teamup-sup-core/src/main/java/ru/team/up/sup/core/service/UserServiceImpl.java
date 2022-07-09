package ru.team.up.sup.core.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.exception.NoContentException;
import ru.team.up.sup.core.exception.UserNotFoundException;
import ru.team.up.sup.core.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexey Tkachenko
 * Класс сервиса для управления пользователями ru.team.up.core.entity.User
 */

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    /**
     * @return Возвращает коллекцию User.
     * Если коллекция пуста, генерирует исключение со статусом HttpStatus.NO_CONTENT
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.debug("Старт метода List<User> getAllUsers()");

        List<User> users = Optional.of(userRepository.findAll())
                .orElseThrow(NoContentException::new);
        log.debug("Получили список всех юзеров из БД {}", users);

        return users;
    }

    /**
     * @param id Уникальный ключ ID пользователя
     * @return Находит в БД пользователя по ID и возвращает его.
     * Если пользователь с переданным ID не найден в базе, генерирует исключение со статусом HttpStatus.NOT_FOUND
     */
    @Override
    @Transactional(readOnly = true)
    public User getOneUser(Long id) {
        log.debug("Старт метода User getOneUser(Long id) с параметром {}", id);

        User user = Optional.of(userRepository.getOne(id))
                .orElseThrow(() -> new UserNotFoundException(id));
        log.debug("Получили юзера из БД {}", user);

        return user;
    }

    /**
     * @param user Объект класса ru.team.up.core.entity.User
     * @return Возвращает сохраненный в БД объект user
     */
    @Override
    @Transactional
    public User saveUser(User user) {
        log.debug("Старт метода User saveUser(User user) с параметром {}", user);

        User save = userRepository.save(user);
        log.debug("Сохранили юзера в БД {}", save);

        return save;
    }

    /**
     * @param id Объекта класса ru.team.up.core.entity.User
     *           Метод удаляет пользователя из БД
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.debug("Старт метода void deleteUser(User user) с параметром {}", id);

        userRepository.deleteById(id);
        log.debug("Удалили юзера из БД с ID {}", id);
    }

    @Override
    public User getUserByName(String name) {
        log.debug("Старт поиска юзера по имени {}", name);

        return userRepository.findUserByName(name);
    }

}
