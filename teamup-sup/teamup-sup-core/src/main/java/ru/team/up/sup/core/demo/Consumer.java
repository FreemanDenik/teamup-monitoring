package ru.team.up.sup.core.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.team.up.dto.SupParameterDto;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author Stepan Glushchenko
 * Consumer для демонстрации работы kafka
 */

public class Consumer {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Consumer.class);
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-sup");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        KafkaConsumer<String, SupParameterDto<?>> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("sup-parameter"));

        while (true) {
            ConsumerRecords<String, SupParameterDto<?>> records = consumer.poll(Duration.ofMillis(300));
            for (ConsumerRecord<String, SupParameterDto<?>> record : records) {
                logger.info("key: " + record.key() + " value: " + record.value() + " partition: " +
                        record.partition() + " offset: " + record.offset());

                SupParameterDto<?> supParameter = record.value();
                System.out.println(supParameter);
            }
        }
    }
}