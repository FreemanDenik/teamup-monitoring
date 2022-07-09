package ru.team.up.sup.core.utils;

import lombok.extern.slf4j.Slf4j;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.core.entity.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ParameterToDto {
    public static SupParameterDto<?> convert(Parameter parameter) {
        SupParameterDto dto = SupParameterDto.builder()
                .parameterName(parameter.getParameterName())
                .parameterType(parameter.getParameterType())
                .systemName(parameter.getSystemName())
                .updateTime(parameter.getUpdateDate())
                .build();
        switch (dto.getParameterType()) {
            case DOUBLE:
                dto.setParameterValue(Double.parseDouble(parameter.getParameterValue()));
                break;
            case BOOLEAN:
                dto.setParameterValue(Boolean.parseBoolean(parameter.getParameterValue()));
                break;
            case INTEGER:
                dto.setParameterValue(Integer.parseInt(parameter.getParameterValue()));
                break;
            case STRING:
                dto.setParameterValue(parameter.getParameterValue());
                break;
        }
        return dto;
    }

    public static List<ListSupParameterDto> parseParameterListToListsDto(List<Parameter> parameterList) {
        if (parameterList == null || parameterList.isEmpty()) {
            log.debug("На вход метода parseParameterListToListsDto пришел null или пустой лист");
            throw new RuntimeException("На вход метода parseParameterListToListsDto пришел null или пустой лист");
        }
        List<ListSupParameterDto> resultList = new ArrayList<>();
        for (AppModuleNameDto module : AppModuleNameDto.values()) {
            log.debug("Проверка наличия параметров из модуля {}", module);
            List<Parameter> filteredList = parameterList.stream().filter(
                            p -> p.getSystemName() == module)
                    .collect(Collectors.toList());
            if (filteredList.isEmpty()) {
                log.debug("Параметры из модуля {} не найдены", module);
                continue;
            }
            log.debug("Найдены параметры из модуля {}", module);
            ListSupParameterDto list = new ListSupParameterDto();
            for (Parameter parameter : filteredList) {
                list.addParameter(convert(parameter));
                log.debug("Параметр {} добавлен в лист модуля {}", parameter, module);
            }
            resultList.add(list);
        }
        if (resultList.isEmpty()) {
            throw new RuntimeException("Ни один параметр не добавлен в результирующий лист");
        }
        log.debug("Размер результирующего листа {}", resultList.size());
        return resultList;
    }
}
