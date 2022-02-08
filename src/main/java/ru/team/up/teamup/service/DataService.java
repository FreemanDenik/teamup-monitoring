package ru.team.up.teamup.service;


import ru.team.up.teamup.entity.Control;
import ru.team.up.teamup.entity.Report;

import java.util.Date;
import java.util.List;


/**
 * Интерфейс DataService
 */
public interface DataService {

    /**
     * Метод saveMessage сохраняет сущность Событие для Кафки в БД
     * @param data принимает событие для сохранения в БД
     * @return возвращает сохранённый экземпляр события в БД
     */
    // По умолчанию save возвращает Report, а для чего?
    Report saveMessage(Report data);
    List<Report> getAll();
    List<Report> findByParam(Control control, Date timeAfter, Date timeBefore);
}
