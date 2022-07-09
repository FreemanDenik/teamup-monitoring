package ru.team.up.sup.core.service;

import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.sup.core.entity.Parameter;

import java.util.List;
import java.util.Optional;

public interface ParameterServiceRest {

    /**
     * @param parameterName Имя объекта Parameter
     * @return Возвращает объект Parameter по его имени
     */
    Optional<Parameter> getParameterByParameterName(String parameterName);

    /**
     * @param id Уникальный ключ объекта Parameter
     * @return Возвращает параметр по его ID
     */
    Optional<Parameter> getParameterById(Long id);

    /**
     * @return Возвращает коллекцию объектов Parameter.
     */
    List<Parameter> getAllParameters();

    /**
     * @param strName часть имени объекта Parameter
     * @return List<Parameter> Возвращает список объектов parameter в имени которых содержится strName
     */
    List<Parameter> findByParameterNameContains(String strName);

    /**
     * @param date1 и date2 даты  формата String
     * @return List<Parameter> Возвращает список объектов parameter, которые созданы между датами
     */
    List<Parameter> findByCreationDateBetween(String date1, String date2);

    /**
     * @param date1 и date2 даты  формата String
     * @return List<Parameter> Возвращает список объектов parameter, которые созданы между датами
     */
    List<Parameter> findByUpdateDateBetween(String date1, String date2);

    /**
     * @return Возвращает коллекцию объектов Parameter по имени системы
     *
     */
    List<Parameter> getParametersBySystemName(AppModuleNameDto systemName);
}
