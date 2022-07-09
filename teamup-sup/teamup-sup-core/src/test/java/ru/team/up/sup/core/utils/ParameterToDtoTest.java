package ru.team.up.sup.core.utils;

import org.junit.jupiter.api.Test;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterType;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ParameterToDtoTest {

    private final User testUser = User.builder()
            .id(1L)
            .name("TestName")
            .lastName("TestLastName")
            .email("test@mail.com")
            .password("testPass")
            .lastAccountActivity(LocalDateTime.now())
            .build();
    private final Parameter testParam1 = Parameter.builder()
            .id(1L)
            .parameterName("TestParam1")
            .parameterType(SupParameterType.BOOLEAN)
            .systemName(AppModuleNameDto.TEAMUP_CORE)
            .parameterValue("false")
            .creationDate(LocalDate.now())
            .updateDate(LocalDateTime.now())
            .userWhoLastChangeParameters(testUser)
            .build();
    private final Parameter testParam2 = Parameter.builder()
            .id(2L)
            .parameterName("TestParam2")
            .parameterType(SupParameterType.INTEGER)
            .systemName(AppModuleNameDto.TEAMUP_KAFKA)
            .parameterValue("123")
            .creationDate(LocalDate.now())
            .updateDate(LocalDateTime.now())
            .userWhoLastChangeParameters(testUser)
            .build();
    private final Parameter testParam3 = Parameter.builder()
            .id(3L)
            .parameterName("TestParam3")
            .parameterType(SupParameterType.INTEGER)
            .systemName(AppModuleNameDto.TEAMUP_APP)
            .parameterValue("123")
            .creationDate(LocalDate.now())
            .updateDate(LocalDateTime.now())
            .userWhoLastChangeParameters(testUser)
            .build();

    @Test
    void canParseList() {
        // Given
        List<Parameter> inputList = List.of(testParam1, testParam2, testParam3);
        Set<AppModuleNameDto> moduleNames = new HashSet<>();
        inputList.stream().forEach(p -> moduleNames.add(p.getSystemName()));
        Set<String> parameterNames = new HashSet<>();
        inputList.stream().forEach(p -> parameterNames.add(p.getParameterName()));
        // When
        List<ListSupParameterDto> resultList = ParameterToDto.parseParameterListToListsDto(inputList);
        // Then
        assertThat(resultList.size()).isEqualTo(moduleNames.size());
        for (ListSupParameterDto list : resultList) {
            list.getList().stream().forEach(paramDto -> parameterNames.remove(paramDto.getParameterName()));
        }
        assertThat(parameterNames.isEmpty());
    }

    @Test
    void willThrownWhenInputListIsNullOrEmpty() {
        assertThatThrownBy(() -> ParameterToDto.parseParameterListToListsDto(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("На вход метода parseParameterListToListsDto пришел null или пустой лист");
        assertThatThrownBy(() -> ParameterToDto.parseParameterListToListsDto(List.of()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("На вход метода parseParameterListToListsDto пришел null или пустой лист");
    }

    @Test
    void willThrownWhenResultListIsEmpty() {
        assertThatThrownBy(() -> ParameterToDto.parseParameterListToListsDto(List.of(Parameter.builder().build())))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ни один параметр не добавлен в результирующий лист");
    }
}