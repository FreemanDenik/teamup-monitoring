package ru.team.up.sup.input.controller.publicController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.service.ParameterService;
import ru.team.up.sup.core.service.ParameterServiceRest;

import java.util.List;
import java.util.Optional;

@Slf4j
@Tag(name = "Public Parameter Controller", description = "public parameter API")
@RestController
@AllArgsConstructor
@RequestMapping("/public/api")
public class ParameterRestControllerPublic {
    private ParameterServiceRest parameterServiceRest;
    private ParameterService parameterService;

    /**
     *
     */

    @GetMapping
    @Operation(summary = "Получение списка всех параметров")
    public ResponseEntity<List<Parameter>> getAllParameters() {
        log.debug("Получен запрос на получение всех параметров");
        ResponseEntity<List<Parameter>> responseEntity;
        List<Parameter> parameterList = parameterServiceRest.getAllParameters();
        if (parameterList.isEmpty()) {
            responseEntity = ResponseEntity.noContent().build();
        } else {
            responseEntity = ResponseEntity.ok(parameterServiceRest.getAllParameters());
        }
        log.debug("Получили ответ {}", responseEntity);
        return responseEntity;
    }

    /**
     *
     */

    @GetMapping("/purge")
    @Operation(summary = "Удаление неиспользуемых параметров")
    public ResponseEntity purgeParameters() {
        log.debug("Получен запрос на удаление неиспользуемых параметров");
        parameterService.purge();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     *
     */
    @GetMapping("/id/{id}")
    @Operation(summary = "Получение параметра по id")
    public ResponseEntity<Parameter> getParameterById(@PathVariable Long id) {
        log.debug("пытаемся получить параметр по id {}", id);

        ResponseEntity<Parameter> responseEntity;
        Optional<Parameter> parameter = parameterServiceRest.getParameterById(id);
        if (parameter.isPresent()) {
            responseEntity = ResponseEntity.ok(parameter.get());
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }
        log.debug("Получили ответ {}", responseEntity);
        return responseEntity;
    }


    @GetMapping("/search/{parameterName}")
    @Operation(summary = "Получение параметров, поиск по части имени")
    public ResponseEntity<List<Parameter>> findByParameterNameContains(@PathVariable String parameterName) {
        log.debug("Поиск параметра по имени или части имени {}", parameterName);
        List<Parameter> parameterList = parameterServiceRest.findByParameterNameContains(parameterName);
        ResponseEntity<List<Parameter>> responseEntity;
        if (parameterList.isEmpty()) {
            responseEntity = ResponseEntity.noContent().build();

        } else {
            responseEntity = ResponseEntity.ok(parameterList);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }


    @GetMapping("/search/creationDate/")
    @Operation(
            summary = "Получение параметров, которые были созданы между датами ",
            description = " Формат даты: 2010-02-05"
    )
    public ResponseEntity<List<Parameter>> findByCreationDateBetween(String date1, String date2) {
        log.debug("Поиск параметров, которые были созданы между датами {} и {}", date1, date2);

        ResponseEntity<List<Parameter>> responseEntity;
        List<Parameter> parameterList = parameterServiceRest.findByCreationDateBetween(date1, date2);

        if (parameterList.isEmpty()) {
            responseEntity = ResponseEntity.noContent().build();
        } else {
            responseEntity = ResponseEntity.ok(parameterList);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }


    @GetMapping("/search/updateData/")
    @Operation(
            summary = "Получение параметров, которые были обновлены между датами ",
            description = "Формат даты: 2010-03-27T10:15:30"
    )
    public ResponseEntity<List<Parameter>> findByUpdateDateBetween(String updateDate1, String updateDate2) {
        log.debug("Поиск параметров, которые были обновлены между датами {} и {}", updateDate1, updateDate2);

        ResponseEntity<List<Parameter>> responseEntity;
        List<Parameter> parameterList = parameterServiceRest.findByUpdateDateBetween(updateDate1, updateDate2);

        if (parameterList.isEmpty()) {
            responseEntity = ResponseEntity.noContent().build();
        } else {
            responseEntity = ResponseEntity.ok(parameterList);
        }
        log.debug("Получили ответ {}", responseEntity);

        return responseEntity;
    }

    @GetMapping("/search/systemName/")
    @Operation(
            summary = "Получение параметров по имени системы, которые были заведены в ДТО "
    )
    public ResponseEntity<List<Parameter>> findBySystemName(AppModuleNameDto systemName) {
        log.debug("Получение параметров по имени системы {}", systemName);
        ResponseEntity<List<Parameter>> responseEntity;
        List<Parameter> parameterList = parameterServiceRest.getParametersBySystemName(systemName);
        if (parameterList.isEmpty()) {
            responseEntity = ResponseEntity.noContent().build();
        } else {
            responseEntity = ResponseEntity.ok(parameterList);
        }

        log.debug("Получили ответ {}", responseEntity);
        return responseEntity;
    }

    @PostMapping(
            value = "/update/{systemName}/")
    @Operation(
            summary = "Получение параметров по умолчанию от модуля"
    )
    public ListSupParameterDto processDefaultParamBySystemName(
            @PathVariable(value = "systemName") AppModuleNameDto systemName,
            @RequestBody ListSupParameterDto listDto) {
        log.debug("Получили параметры по умолчанию от модуля {}", systemName);
        parameterService.compareWithDefaultAndUpdate(listDto);
        return listDto;
    }

}
