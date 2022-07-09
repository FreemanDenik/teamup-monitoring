package ru.team.up.sup.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterType;
import ru.team.up.sup.core.entity.Parameter;
import ru.team.up.sup.core.entity.User;
import ru.team.up.sup.core.utils.ParameterToDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaSupServiceImplTest {

    @Mock
    private KafkaTemplate<AppModuleNameDto, ListSupParameterDto> kafkaTemplate;
    private KafkaSupService underTest;
    @Value(value = "${kafka.topic.name}")
    private String testTopic;
    private ListSupParameterDto listToSend;
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
            .parameterType(SupParameterType.STRING)
            .systemName(AppModuleNameDto.TEAMUP_SUP)
            .parameterValue("Hello world!")
            .creationDate(LocalDate.now())
            .updateDate(LocalDateTime.now())
            .userWhoLastChangeParameters(testUser)
            .build();

    @BeforeEach
    void setUp() {
        underTest = new KafkaSupServiceImpl(kafkaTemplate);
        listToSend = new ListSupParameterDto();
    }

    @Test
    void canSendOneParameter() {
        // Given
        listToSend.addParameter(ParameterToDto.convert(testParam1));
        // When
        underTest.send(testParam1);
        // Then
        ArgumentCaptor<String> topicArgumentCaptor =
                ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<AppModuleNameDto> appModuleNameDtoArgumentCaptor =
                ArgumentCaptor.forClass(AppModuleNameDto.class);
        ArgumentCaptor<ListSupParameterDto> listSupParameterDtoArgumentCaptor =
                ArgumentCaptor.forClass(ListSupParameterDto.class);
        verify(kafkaTemplate).send(topicArgumentCaptor.capture(),
                appModuleNameDtoArgumentCaptor.capture(),
                listSupParameterDtoArgumentCaptor.capture());
        String capturedTopic = topicArgumentCaptor.getValue();
        assertThat(capturedTopic).isEqualTo(testTopic);
        AppModuleNameDto capturedModule = appModuleNameDtoArgumentCaptor.getValue();
        assertThat(capturedModule).isEqualTo(testParam1.getSystemName());
        ListSupParameterDto capturedList = listSupParameterDtoArgumentCaptor.getValue();
        assertThat(capturedList).isEqualTo(listToSend);
    }

    @Test
    void willThrownWhenParameterIsNull() {
        assertThatThrownBy(() -> underTest.send((Parameter) null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("В метод send вместо параметра пришел null");
    }

    @Test
    void canSendListOfParameters() {
        // Given
        List<Parameter> inputList = List.of(testParam1, testParam2, testParam3);
        inputList.stream().forEach(parameter -> listToSend.addParameter(ParameterToDto.convert(parameter)));
        // When
        underTest.send(inputList);
        // Then
        for (ListSupParameterDto list : ParameterToDto.parseParameterListToListsDto(inputList)) {
            verify(kafkaTemplate).send(testTopic, list.getModuleName(), list);
        }
    }

    @Test
    void willThrownWhenParameterListIsEmptyOrNull() {
        assertThatThrownBy(() -> underTest.send((List<Parameter>) null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("В метод send пришел пустой лист или null.");
        assertThatThrownBy(() -> underTest.send(List.of()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("В метод send пришел пустой лист или null.");
    }
}