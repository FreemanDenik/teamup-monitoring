package ru.team.up.sup.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.team.up.dto.AppModuleNameDto;

@Slf4j
@Service
public class KafkaSupServiceImpl implements KafkaSupService {

    @Value(value = "${sup.kafka.system.name}")
    private AppModuleNameDto systemName;
    @Value(value = "${sup.kafka.topic.name}")
    private String topic;
    private final KafkaTemplate<AppModuleNameDto, AppModuleNameDto> kafkaTemplate;

    @Autowired
    public KafkaSupServiceImpl(KafkaTemplate<AppModuleNameDto, AppModuleNameDto> kafkaModuleNameTemplate) {
        this.kafkaTemplate = kafkaModuleNameTemplate;
    }

    @Override
    public void getAllModuleParameters() {
        kafkaTemplate.send(topic, AppModuleNameDto.TEAMUP_SUP, systemName);
    }
}