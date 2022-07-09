package ru.team.up.sup.input.controller.privateController;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.SupParameterType;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.service.KafkaSupService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Tag(name = "Kafka Private Controller", description = "Kafka API")
@RestController
@RequestMapping("/private/kafka")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaController {

    private KafkaSupService kafkaProducerSupService;


    @Operation(
            summary = "(Для тестов) Отправка параметра напрямую в кафку. ",
            description = " \n" +
                    "        \"id\": 0,\n" +
                    "            \"parameterName\": \"book\",\n" +
                    "            \"parameterType\": \"STRING\",\n" +
                    "            \"systemName\": \"TEAMUP_APP\",\n" +
                    "            \"parameterValue\": \"Девушка с татуировкой вагона\",\n" +
                    "            \"creationDate\": \"2015-02-20\",\n" +
                    "            \"updateDate\": \"2015-02-20T16:17:36.139Z\",\n" +
                    "            \"userWhoLastChangeParameters\": null\n" +
                    "    "
    )
    @PostMapping("/send")
    public ResponseEntity<Parameter> send(@RequestBody(required = false) Parameter parameter) {
        log.debug("Старт метода ResponseEntity<Parameter> send( Parameter parameter) с {}", parameter);
        kafkaProducerSupService.send(parameter);
        return new ResponseEntity<>(parameter, HttpStatus.OK);
    }

//    @Operation(summary = "Получение списка всех параметров")
//    @PostMapping("/get")
//    public ResponseEntity<List<SupParameterDto<?>>> get() {
//        log.debug("Старт метода ResponseEntity<List<SupParameterDto>> get()");
//        return new ResponseEntity<>(kafkaProducerSupService.getListParameters(), HttpStatus.OK);
//    }

    @Operation(summary = "(Для тестов) Отправка трех параметров")
    @PostMapping("/sendTest")
    public ResponseEntity<Parameter> sendTest() {
        log.debug("Старт метода ResponseEntity<Parameter> sendTest()");
        kafkaProducerSupService.send(Parameter.builder()
                .id(1L)
                .parameterName("testName")
                .parameterType(SupParameterType.STRING)
                .parameterValue("testValue")
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .creationDate(LocalDate.now())
                .updateDate(LocalDateTime.now())
                .build());
        kafkaProducerSupService.send(Parameter.builder()
                .id(2L)
                .parameterName("testName2")
                .parameterType(SupParameterType.STRING)
                .parameterValue("testValue")
                .systemName(AppModuleNameDto.TEAMUP_CORE)
                .creationDate(LocalDate.now())
                .updateDate(LocalDateTime.now())
                .build());
        kafkaProducerSupService.send(Parameter.builder()
                .id(3L)
                .parameterName("testName3")
                .parameterType(SupParameterType.STRING)
                .parameterValue("Ok")
                .systemName(AppModuleNameDto.TEAMUP_DTO)
                .creationDate(LocalDate.now())
                .updateDate(LocalDateTime.now())
                .build());

        return ResponseEntity.ok().build();
    }
}