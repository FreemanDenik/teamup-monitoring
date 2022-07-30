package ru.team.up.core.models;

import lombok.Getter;
import lombok.Setter;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.InitiatorTypeDto;

/**
 * Класс для получения данных фильтрации
 * @author Деник
 * @version 1.0
 */
@Getter
@Setter
public class FindViewModel {
    private AppModuleNameDto moduleName;
    private InitiatorTypeDto initiatorType;
    private String timeAfter;
    private String timeBefore;
    private String paramKey;
    private String paramValue;
}
