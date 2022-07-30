package ru.team.up.sup.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.dto.ListSupParameterDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:sup.properties")
public class KafkaConsumerSupConfig {

    @Value(value = "${sup.kafka.group.id}")
    private String groupId;
    @Value(value = "${sup.kafka.bootstrapAddress}")
    private String bootstrapAddress;

    public Map<String, Object> jsonConsumerConfig() {
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
    public ConsumerFactory<AppModuleNameDto, ListSupParameterDto> listDtoConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(jsonConsumerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<AppModuleNameDto, ListSupParameterDto> listDtoKafkaContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<AppModuleNameDto, ListSupParameterDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(listDtoConsumerFactory());
        factory.setRecordFilterStrategy(param -> param.key() != AppModuleNameDto.TEAMUP_CORE);
        return factory;
    }
}
