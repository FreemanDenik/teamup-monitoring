package ru.team.up.sup.core.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.team.up.dto.AppModuleNameDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerSupConfig {

    @Value(value = "${kafka.group.id}")
    private String groupId;
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    public Map<String, Object> jsonConfigProps() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return configProps;
    }

    @Bean
    public ConsumerFactory<AppModuleNameDto, AppModuleNameDto> moduleConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(jsonConfigProps());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<AppModuleNameDto, AppModuleNameDto> kafkaModuleContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<AppModuleNameDto, AppModuleNameDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setRecordFilterStrategy(message -> message.key() != AppModuleNameDto.TEAMUP_SUP);
        factory.setConsumerFactory(moduleConsumerFactory());
        return factory;
    }
}
