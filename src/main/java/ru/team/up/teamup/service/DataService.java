package ru.team.up.teamup.service;


import ru.team.up.teamup.entity.Report;


/**
 * Интерфейс DataService
 */
public interface DataService {

    /**
     * Метод saveMessage сохраняет сущность Событие для Кафки в БД
     * @param data принимает событие для сохранения в БД
     * @return возвращает сохранённый экземпляр события в БД
     */
    Report saveMessage(Report data);
}
