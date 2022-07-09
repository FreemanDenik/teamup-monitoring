package ru.team.up.sup.input.controller.privateController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Alexey Tkachenko
 *
 * @link localhost:8083/swagger-ui.html
 * Документация API
 */

@Slf4j
@Tag(name = "Out Controller",description = "Out API")
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/private/account/admin")
public class OutController {
    private UserService userService;
    /**
     * @return Результат работы метода userService.getAllUsers() в виде коллекции пользователей
     * в теле ResponseEntity
     */
    @Operation(summary ="Получение списка всех пользователей")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.debug("Старт метода ResponseEntity<List<User>> getAllUsers()");

        ResponseEntity<List<User>> responseEntity = ResponseEntity.ok(userService.getAllUsers());
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param id Значение ID пользователя
     * @return Результат работы метода userService.getOneUser(id) в виде объекта User
     * в теле ResponseEntity
     */
    @Operation(summary ="Получение пользователя по id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable Long id) {
        log.debug("Старт метода ResponseEntity<User> getOneUser(@PathVariable Long id) с параметром {}", id);

        ResponseEntity<User> responseEntity = ResponseEntity.ok(userService.getOneUser(id));
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param user Создаваемый объект класса User
     * @return Результат работы метода userService.saveUser(user) в виде объекта User
     * в теле ResponseEntity
     */
    @Operation(summary ="Создание нового пользователя")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestParam String user, @RequestBody @NotNull User userCreate) {
        log.debug("Старт метода ResponseEntity<User> createUser(@RequestBody @NotNull User user) с параметром {}", userCreate);

        ResponseEntity<User> responseEntity = new ResponseEntity<>(userService.saveUser(userCreate), HttpStatus.CREATED);
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param user Обновляемый объект класса User
     * @return Результат работы метода userService.saveUser(user) в виде объекта User
     * в теле ResponseEntity
     */
    @Operation(summary ="Обновление данных пользователя")
    @PatchMapping
    public ResponseEntity<User> updateUser(@RequestBody @NotNull User user) {
        log.debug("Старт метода ResponseEntity<User> updateUser(@RequestBody @NotNull User user) с параметром {}", user);

        log.debug("Проверка наличия обновляемого пользователя в БД");
        userService.getOneUser(user.getId());

        ResponseEntity<User> responseEntity = ResponseEntity.ok(userService.saveUser(user));
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param id Удаляемый объект класса User
     * @return Объект ResponseEntity со статусом OK
     */
    @Operation(summary ="Удаление пользователя по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        log.debug("Старт метода ResponseEntity<User> updateUser(@RequestBody @NotNull User user) с параметром {}", id);

        userService.deleteUser(id);

        ResponseEntity<User> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }
}
