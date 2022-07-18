package ru.team.up.teamup.service;

import com.querydsl.core.BooleanBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.team.up.core.service.DataService;
import ru.team.up.core.service.DataServiceImpl;
import ru.team.up.dto.*;
import ru.team.up.core.entity.Report;
import ru.team.up.core.repositories.DataRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервисов")
class DataServiceTest {

    @Mock
    private DataRepository dataTestRepository;

    private DataService dataTestService;

    @Captor
    ArgumentCaptor<BooleanBuilder> booleanBuilderCaptor;

    @Captor
    ArgumentCaptor<Report> reportCaptor;

    @BeforeEach
    void setUp() {
        dataTestService = new DataServiceImpl(dataTestRepository);
    }

    @Test
    @DisplayName("Тест метода saveMessage сервиса DataService")
    void saveMessage() {
        //given
        Report reportTest = Report.builder()
                .reportName("Событие №1")
                .reportStatus(ReportStatusDto.SUCCESS)
                .appModuleName(AppModuleNameDto.TEAMUP_MONITORING)
                .control(ControlDto.AUTO)
                .initiatorId(2L)
                .initiatorType(InitiatorTypeDto.USER)
                .initiatorName("user")
                .time(new Date())
                .parameters(null)
                .build();

        //when
        dataTestService.saveMessage(reportTest);

        //then
        verify(dataTestRepository).save(reportCaptor.capture());
        assertThat(reportTest).isEqualTo(reportCaptor.getValue());
    }

    @Test
    @DisplayName("Тест метода getAll сервиса DataService")
    void getAll() {
        //when
        dataTestService.getAll(5);
        //then
        verify(dataTestRepository).findAll();
    }

    @Test
    @SneakyThrows
    @DisplayName("Тест метода findByParam сервиса DataService")
    void findByParam() {

        //given
        List<Report> expectedReports = List.of(Report.builder()
                        .reportName("Событие №1")
                        .reportStatus(ReportStatusDto.SUCCESS)
                        .appModuleName(AppModuleNameDto.TEAMUP_MONITORING)
                        .control(ControlDto.AUTO)
                        .initiatorId(2L)
                        .initiatorType(InitiatorTypeDto.USER)
                        .initiatorName("user")
                        .time(new Date())
                        .parameters(new HashMap<>(){{
                            put("count", ParametersDto.builder()
                                    .description("Кол-во")
                                    .value("22")
                                    .build());
                            put("two",ParametersDto.builder()
                                    .description("Тест")
                                    .value("second")
                                    .build());
                        }})
                        .build(),
                Report.builder()
                        .reportName("Событие №2")
                        .reportStatus(ReportStatusDto.FAILURE)
                        .appModuleName(AppModuleNameDto.TEAMUP_MONITORING)
                        .control(ControlDto.AUTO)
                        .initiatorId(3L)
                        .initiatorType(InitiatorTypeDto.USER)
                        .initiatorName("user2")
                        .time(new Date())
                        .parameters(new HashMap<>(){{
                            put("count", ParametersDto.builder()
                                    .description("Кол-во")
                                    .value("22")
                                    .build());
                            put("two",ParametersDto.builder()
                                    .description("Тест")
                                    .value("second")
                                    .build());
                        }})
                        .build());

        given(dataTestRepository.findAll(any(BooleanBuilder.class)))
                .willReturn(expectedReports);

        //when
        List<Report> actualReports = (List<Report>) dataTestService.findByParam(
                AppModuleNameDto.TEAMUP_MONITORING,
                InitiatorTypeDto.USER,
                "2022-05-09",
                "2022-05-11",
                "count", "22"
                ,0,5
        );

        //then
        verify(dataTestRepository, times(1)).findAll(any(BooleanBuilder.class));
        verify(dataTestRepository, times(1)).findAll(booleanBuilderCaptor.capture());

        assertThat(actualReports)
                .isEqualTo(expectedReports);

        assertThat(
                booleanBuilderCaptor.getValue()).asString()
                .contains(AppModuleNameDto.TEAMUP_MONITORING.toString())
                .contains(InitiatorTypeDto.USER.toString());
    }
}