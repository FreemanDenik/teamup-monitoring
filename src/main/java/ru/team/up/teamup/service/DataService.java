package ru.team.up.teamup.service;


import ru.team.up.teamup.entity.AppModuleName;
import ru.team.up.teamup.entity.InitiatorType;
import ru.team.up.teamup.entity.Report;

import java.text.ParseException;
import java.util.List;


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
    List<Report> getAll();
    List<Report> findByParam(AppModuleName moduleName, InitiatorType initiatorType, String timeAfter, String timeBefore) throws ParseException;
}
