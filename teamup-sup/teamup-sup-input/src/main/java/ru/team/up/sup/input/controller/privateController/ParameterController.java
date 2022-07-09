package ru.team.up.sup.input.controller.privateController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.service.ParameterService;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 *
 * @link localhost:8083/swagger-ui.html
 * Документация API
 */

@Slf4j
@RestController
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Parameter Controller", description = "Parameter API")
@RequestMapping(value = "/private/account/user/parameters")
public class ParameterController {
    private ParameterService parameterService;

    /**
     * @return Результат работы метода parameterService.getAllParameters() в виде коллекции параметров
     * в теле ResponseEntity
     */
    @GetMapping
    @Operation(summary ="Получение списка всех параметров")
    public ResponseEntity<List<Parameter>> getAllParameters() {
        log.debug("Старт метода ResponseEntity<List<Parameter>> getAllParameters()");

        ResponseEntity<List<Parameter>> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(parameterService.getAllParameters());
        } catch (PersistenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param id Значение ID параметра
     * @return Результат работы метода parameterService.getOneParameter(id) в виде объекта Parameter
     * в теле ResponseEntity
     */
    @GetMapping("/{id}")
    @Operation(summary ="Получение параметра по id")
    public ResponseEntity<Parameter> getParameterById(@PathVariable Long id) {
        log.debug("Старт метода ResponseEntity<Parameter> getParameterById(@PathVariable Long id) с параметром {}", id);

        ResponseEntity<Parameter> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(parameterService.getParameterById(id));
        } catch (PersistenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param systemName Значение systemName параметра
     * @return Результат работы метода parameterService.getParametersBySystemName(systemName) в виде коллекции параметров
     * в теле ResponseEntity
     */
    @GetMapping("/{systemName}")
    @Operation(summary ="Получение коллекции параметров по systemName")
    public ResponseEntity<List<Parameter>> getParametersBySystemName(@PathVariable String systemName) {
        log.debug("Старт метода ResponseEntity<Parameter> getParametersBySystemName(@PathVariable String systemName) с параметром {}", systemName);

        ResponseEntity<List<Parameter>> responseEntity;
        try {
            responseEntity = ResponseEntity.ok(parameterService.getParametersBySystemName(AppModuleNameDto.valueOf(systemName)));
        } catch (PersistenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    /**
     * @param parameterName Значение parameterName параметра
     * @return Результат работы метода parameterService.getParameterByParameterName(parameterName) в виде объекта Parameter
     * в теле ResponseEntity
     */
    @GetMapping("/{parameterName}")
    @Operation(summary ="Получение параметра по parameterName")
    public ResponseEntity<Parameter> getParameterByParameterName(@PathVariable String parameterName) {
        log.debug("Старт метода ResponseEntity<Parameter> getParameterByParameterName(@PathVariable String parameterName) с параметром {}", parameterName);

        ResponseEntity<Parameter> responseEntity;
        try {
        responseEntity = ResponseEntity.ok(parameterService.getParameterByParameterName(parameterName));
        } catch (PersistenceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }
}
