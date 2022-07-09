package ru.team.up.sup.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.utils.ParameterToDto;

import java.util.List;

/**
 * Сервис для отправки параметров в kafka
 */

@Slf4j
@Service
public class KafkaSupServiceImpl implements KafkaSupService {
    /**
     * Имя топика kafka
     */
    @Value(value = "${kafka.topic.name}")
    private String TOPIC;

    /**
     * Шаблон kafka для отправки параметров
     */
    private final KafkaTemplate<AppModuleNameDto, ListSupParameterDto> listSupParameterDtoKafkaTemplate;


    @Autowired
    public KafkaSupServiceImpl(KafkaTemplate<AppModuleNameDto, ListSupParameterDto> listSupParameterDtoKafkaTemplate) {
        this.listSupParameterDtoKafkaTemplate = listSupParameterDtoKafkaTemplate;
    }

    /**
     * Отправка одного параметра в kafka с предварительной конвертацией в ListDTO объект
     *
     * @param parameter объект для конфигурации работы модулей приложения
     */
    @Override
    public void send(Parameter parameter) {
        log.debug("Начало отправки параметра: {}", parameter);
        if (parameter == null) {
            log.debug("В метод send вместо параметра пришел null");
            throw new RuntimeException("В метод send вместо параметра пришел null");
        }
        ListSupParameterDto listToSend = new ListSupParameterDto();
        listToSend.addParameter(ParameterToDto.convert(parameter));
        listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend.getModuleName(), listToSend);
        log.debug("Завершение отправки параметра: {}", parameter);
    }

    /**
     * Отправка листа параметров в kafka с предварительной конвертацией в ListDTO объект
     *
     * @param list объект для конфигурации работы модулей приложения
     */
    @Override
    public void send(List<Parameter> list) {
        log.debug("Начало отправки листа параметров");
        if (list == null || list.isEmpty()) {
            log.debug("В метод send пришел пустой лист или null.");
            throw new RuntimeException("В метод send пришел пустой лист или null.");
        }
        for (ListSupParameterDto listToSend : ParameterToDto.parseParameterListToListsDto(list)) {
            listSupParameterDtoKafkaTemplate.send(TOPIC, listToSend.getModuleName(), listToSend);
            log.debug("Отправлены параметры в модуль {}", listToSend.getModuleName());
        }
        log.debug("Завершение отправки листа параметров");
    }

}