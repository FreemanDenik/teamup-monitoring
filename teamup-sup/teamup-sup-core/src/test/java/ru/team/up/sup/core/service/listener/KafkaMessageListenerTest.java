package ru.team.up.sup.core.service.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.sup.core.service.KafkaSupService;
import ru.team.up.sup.core.service.ParameterService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaMessageListenerTest {

    @Mock
    private KafkaSupService kafkaSupService;
    @Mock
    private ParameterService parameterService;
    private final AppModuleNameDto testModule = AppModuleNameDto.TEAMUP_CORE;
    KafkaMessageListener underTest;

    @BeforeEach
    void setUp() {
        underTest = new KafkaMessageListener(kafkaSupService, parameterService);
    }

    @Test
    void canSendModuleParametersViaListener() {
        // When
        underTest.listenForModuleParameterRequests(testModule);
        // Then
        ArgumentCaptor<AppModuleNameDto> appModuleNameDtoArgumentCaptor =
                ArgumentCaptor.forClass(AppModuleNameDto.class);
        verify(parameterService).getParametersBySystemName(appModuleNameDtoArgumentCaptor.capture());
        AppModuleNameDto capturedModule = appModuleNameDtoArgumentCaptor.getValue();
        assertThat(capturedModule).isEqualTo(testModule);
        verify(kafkaSupService).send(List.of());
    }

    @Test
    void willThrowWhenModuleIsNull() {
        assertThatThrownBy(() -> underTest.listenForModuleParameterRequests(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Модуль запрашивающий настройки = null");
    }
}