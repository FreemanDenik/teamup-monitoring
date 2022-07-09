package ru.team.up.teamup.models;

import lombok.Data;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.InitiatorTypeDto;

@Data
public class FindViewModel {
    private AppModuleNameDto moduleName;
    private InitiatorTypeDto initiatorType;
    private String timeAfter;
    private String timeBefore;
    private String paramKey;
    private String paramValue;


}
