package ru.team.up.sup.core.service;

import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.exception.NoContentException;

import java.util.List;

/**
 * Интерфейс для управления параметрами ru.team.up.core.entity.Parameter
 */
public interface ParameterService {

    /**
     * @return Возвращает коллекцию объектов Parameter.
     * Если коллекция пуста, генерирует исключение со статусом HttpStatus.NO_CONTENT
     */
    List<Parameter> getAllParameters() throws NoContentException;

    /**
     * @return Возвращает коллекцию объектов Parameter по имени системы
     * Если коллекция пуста, генерирует исключение со статусом HttpStatus.NO_CONTENT
     */
    List<Parameter> getParametersBySystemName(AppModuleNameDto systemName) throws NoContentException;

    /**
     * @param id Уникальный ключ объекта Parameter
     * @return Возвращает параметр по его ID
     * Если параметр с переданным ID не найден в базе, генерирует исключение со статусом HttpStatus.NO_CONTENT
     */
    Parameter getParameterById(Long id) throws NoContentException;

    /**
     * @param parameterName Имя объекта Parameter
     * @return Возвращает объект Parameter по его имени
     * Если параметр с переданным именем не найден в базе, генерирует исключение со статусом HttpStatus.NO_CONTENT
     */
    Parameter getParameterByParameterName(String parameterName) throws NoContentException;

    /**
     * @param parameter Объект класса ru.team.up.core.entity.Parameter
     * @return Возвращает сохраненный в БД объект parameter
     */
    Parameter saveParameter(Parameter parameter);

    /**
     * @param id Объект класса ru.team.up.core.entity.Parameter
     *           Метод удаляет параметр из БД
     */
    void deleteParameter(Long id);

    /**
     * @param parameter Объект класса ru.team.up.core.entity.Parameter
     * @return Возвращает сохраненный в БД объект parameter
     */
    Parameter editParameter(Parameter parameter);

    /**
     * @param dtoList Объект класса ru.team.up.dto.ListSupParameterDto
     *                Метод сравнивает параметры по умолчанию полученные от модуля
     *                Параметры, которых нет в БД добавляются автоматически
     *                Неиспользуемые модулем параметры получают флаг inUse = false
     *                Используемые модулем параметры получают флаг inUse = true и обновляют дату lastUsedDate
     */
    void compareWithDefaultAndUpdate(ListSupParameterDto dtoList);

    /**
     * Метод удаляет все параметры в БД с флагом inUse = false и датой lastUsedDate более 7 дней
     */
    void purge();
}
