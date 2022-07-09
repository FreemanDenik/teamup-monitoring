package ru.team.up.sup.core.service;


import ru.team.up.sup.core.entity.Parameter;

import java.util.List;

/**
 * Интерфейс сервиса для отправки параметров в kafka
 */

public interface KafkaSupService {
    /**
     * Отправка параметра в kafka
     *
     * @param parameter объект для конфигурации работы модулей приложения
     */
    void send(Parameter parameter);

    /**
     * Отправка нескольких параметров в kafka
     *
     * @param list лист с объектами для конфигурации работы модулей приложения
     */
    void send(List<Parameter> list);
}