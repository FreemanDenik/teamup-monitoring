package ru.team.up.core.embeddedKafka;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.team.up.dto.*;
import ru.team.up.core.entity.Report;
import ru.team.up.core.service.DataService;
import ru.team.up.core.tasks.MessageListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {"kafka.bootstrapAddress=localhost:9093"})
@EmbeddedKafka(topics = "${kafka.topic.name}", count = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
@Import(EmbeddedKafkaTestConfiguration.class)
@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("Тест консьюмера мониторинга с использованием in-memory брокера")
public class EmbeddedKafkaTest {

    @Autowired
    private KafkaTemplate<String, Report> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    ConsumerFactory consumerFactory;

    @Value("${kafka.topic.name}")
    private String topic;

    @Mock
    private static DataService dataService;

    @Autowired
    @InjectMocks
    MessageListener messageListener;

    @Captor
    ArgumentCaptor<Report> reportCaptor;

    private Report reportTest;

    @Test
    @DisplayName("Отправка и получение тестового отчёта")
    public void test() {

        //given
        //создаём тестовый отчёт
        Map<String, ParametersDto> monitoringParameters = new HashMap<>();
        monitoringParameters.put("count", ParametersDto.builder()
                        .description("Кол-во мероприятий")
                        .value(10)
                .build());

        reportTest = Report.builder()
                .reportName("Событие №1")
                .reportStatus(ReportStatusDto.SUCCESS)
                .appModuleName(AppModuleNameDto.TEAMUP_MONITORING)
                .control(ControlDto.AUTO)
                .initiatorId(2L)
                .initiatorType(InitiatorTypeDto.USER)
                .initiatorName("user")
                .time(new Date())
                .parameters(monitoringParameters)
                .build();

        //when
        //отправляем тестовый отчёт в кафку
        kafkaTemplate
                .send(topic, reportTest)
                .addCallback(
                new ListenableFutureCallback<>() {
                    @Override
                    public void onSuccess(SendResult result) {
                        log.debug("Отчёт {} успешно отправлен. Метаданные записи: {}", reportTest,
                                result.getRecordMetadata());
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        log.debug("При отправке отчёта {} возникла следующая ошибка {}", reportTest, ex.getMessage());
                    }
                }
        );

        //подключаем консьюмера из основного тестируемого модуля к нашему in-memory брокеру
        embeddedKafka.consumeFromAllEmbeddedTopics(consumerFactory.createConsumer());

        //then
        //проверяем что быз вызван сервис сохранения отчёта в БД (который вызывается консьюмером)
        //и что этот отчёт совпадает с отправленным  в наш виртуальный брокер
        verify(dataService, times(1)).saveMessage(reportCaptor.capture());
        assertThat(reportTest).isEqualTo(reportCaptor.getValue());
    }
}
