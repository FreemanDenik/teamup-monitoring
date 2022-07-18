package ru.team.up.core.service;


import org.springframework.data.domain.Page;
import ru.team.up.core.entity.Report;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.InitiatorTypeDto;

import java.text.ParseException;


/**
 * Интерфейс DataService
 */
public interface DataService {

    /**
     * Метод saveMessage сохраняет сущность Событие для Кафки в БД
     * @param data принимает событие для сохранения в БД
     */
    // По умолчанию save возвращает Report, а для чего?
    void saveMessage(Report data);
    Page<Report> getAll(int pageSize);
    Page<Report> findByParam(AppModuleNameDto moduleName, InitiatorTypeDto initiatorType,
                             String timeAfter, String timeBefore,
                             String paramKey, String paramValue, int page, int pageSize) throws ParseException;
}
