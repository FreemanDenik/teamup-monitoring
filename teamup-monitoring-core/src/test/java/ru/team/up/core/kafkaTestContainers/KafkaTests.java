package ru.team.up.core.kafkaTestContainers;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.team.up.dto.*;
import ru.team.up.core.entity.Report;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
public class KafkaTests {

    private KafkaConsumer<String, Report> kafkaTestConsumer;

    private KafkaProducer<String, Report> kafkaTestProducer;

    private AdminClient kafkaAdmin;

    private String topicName;

    Report reportTest;

    static class ReportHelper{
        static Report consumedReport;
    }

    @Container
    private static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
            .withEmbeddedZookeeper();

    @BeforeAll
    public void kafkaTestSetUp() throws Exception {

        kafkaAdmin = kafkaAdmin();
        kafkaTestTopic();
        kafkaTestProducer = kafkaTestProducer();
        kafkaTestConsumer = kafkaTestConsumer();

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
    }

    @Test
    @DisplayName("Отправка продюсером и получение консьюмером отчёта")
    void testProduceConsumeKafkaRecord() {

        ProducerRecord<String, Report> producerRecord = new ProducerRecord<>(topicName,
                InitiatorTypeDto.ADMIN.toString(),
                reportTest);

        //отправляем одну запись в кафку
        kafkaTestProducer.send(producerRecord, (recordMetadata, e) -> {
            if (e != null){
                log.debug("Возникло исключение при попытке отправить тестовый отчёт продюсером {}", e.getMessage());
            } else {
                log.debug("Запись {} отправлено продюсером. Метаданные topic: {} partition: {} offset: {} time {}",
                        producerRecord,
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset(),
                        new Date(recordMetadata.timestamp()));
            }
        });

        //проверяем единственную запись в consumer-е - значение должно совпасть с отправленным продюсером
        ConsumerRecords<String, Report> consumerRecords = kafkaTestConsumer.poll(Duration.ofMillis(2000));
        log.debug("Получено {} записей консьюмером", consumerRecords.count());

        consumerRecords
                .iterator()
                .forEachRemaining(consumerRecord -> ReportHelper.consumedReport = consumerRecord.value());

        assertThat(ReportHelper.consumedReport).isEqualTo(reportTest);
    }

    public AdminClient kafkaAdmin() {
        return AdminClient
                .create(Map.of(
                        AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                        kafka.getBootstrapServers()
                ));
    }

    @SneakyThrows
    public void kafkaTestTopic() {

        Properties properties = new Properties();

        properties
                .load(Thread
                        .currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream("application.properties"));

        topicName = properties.getProperty("kafka.topic.name");

        CreateTopicsResult createTopicsResult = kafkaAdmin.createTopics(
                Collections.singleton(new NewTopic(topicName,
                        Integer.parseInt(properties.getProperty("kafka.partitions")),
                        (short) Integer.parseInt(properties.getProperty("kafka.replicationFactor")))));

        createTopicsResult.values().forEach((topic, kafkaFuture) -> {
            try {
            log.debug("Topic {} был создан с параметрами topicId: {} numPartitions: {} replicationFactor: {}",
                    topic,
                    createTopicsResult.topicId(topic).get(1, TimeUnit.SECONDS),
                    createTopicsResult.numPartitions(topic).get(1, TimeUnit.SECONDS),
                    createTopicsResult.replicationFactor(topic).get(1, TimeUnit.SECONDS));
            } catch (Exception e) {
                log.debug("При получении параметров созданного топика возникло исключение {}", e.getMessage());
            }
        });
    }

    public KafkaConsumer<String, Report> kafkaTestConsumer() {

        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consuming");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, Report> kafkaTestConsumer = new KafkaConsumer<>(properties);

        kafkaTestConsumer.subscribe(Collections.singleton(topicName));

        return kafkaTestConsumer;
    }

    public KafkaProducer<String, Report> kafkaTestProducer() {

        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        properties.put(ProducerConfig.ACKS_CONFIG, "1");

        return new KafkaProducer<>(properties);
    }

}
