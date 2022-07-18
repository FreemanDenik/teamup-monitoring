package ru.team.up.core.embeddedKafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.team.up.core.entity.Report;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class EmbeddedKafkaTestConfiguration {

    @Value("${kafka.bootstrapAddress}")
    private String server;

    private Map<String, Object> producerConfig() {

        Map<String, Object> property = new HashMap<>();
        property.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        property.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        property.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return property;
    }

    private ProducerFactory<String, Report> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, Report> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
