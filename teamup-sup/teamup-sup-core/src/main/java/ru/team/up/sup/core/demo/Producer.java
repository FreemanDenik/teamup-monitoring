package ru.team.up.sup.core.demo;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.SupParameterDto;

import java.util.Properties;

/**
 * @author Stepan Glushchenko
 * Producer для демонстарции работы kafka
 */

public class Producer {
    public static void main(String[] args) throws InterruptedException {
        Logger logger = LoggerFactory.getLogger(Producer.class);
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        KafkaProducer<String, SupParameterDto> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 100; i++) {
            SupParameterDto<Integer> supParameterDto = SupParameterDto.<Integer>builder()
                    .parameterName("SupParameter-" + i)
                    .systemName(
                            AppModuleNameDto.values()[(Double.valueOf(Math.random() * 7)).intValue()])
                    .parameterValue(i)
                    .build();

            ProducerRecord<String, SupParameterDto> record = new ProducerRecord<>("sup-parameter",
                    supParameterDto.getSystemName().name(), supParameterDto);

            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    logger.info("received new metadata, topic: " + metadata.topic() + " partition: " +
                            metadata.partition() + " offsets: " + metadata.offset() + " time: " +
                            metadata.timestamp());
                } else {
                    logger.error("error producing", exception);
                }
            });
            Thread.sleep(5000);
        }
        producer.flush();
        producer.close();
    }
}