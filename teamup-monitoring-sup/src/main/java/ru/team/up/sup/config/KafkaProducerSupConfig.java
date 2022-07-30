package ru.team.up.sup.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.team.up.dto.AppModuleNameDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerSupConfig {

    @Value(value = "${sup.kafka.bootstrapAddress}")
    private String bootstrapAddress;

    public Map<String, Object> producerConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProps;
    }

    public ProducerFactory<AppModuleNameDto, AppModuleNameDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<AppModuleNameDto, AppModuleNameDto> kafkaModuleNameTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
