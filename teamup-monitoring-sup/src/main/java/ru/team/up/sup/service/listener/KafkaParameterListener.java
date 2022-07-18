package ru.team.up.sup.service.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.team.up.dto.ListSupParameterDto;
import ru.team.up.dto.SupParameterDto;
import ru.team.up.sup.service.ParameterService;

import java.util.List;

@Slf4j
@Component
public class KafkaParameterListener {

    private final ParameterService parameterService;

    @Autowired
    public KafkaParameterListener(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @KafkaListener(topics = "${sup.kafka.topic.name}", containerFactory = "listDtoKafkaContainerFactory")
    public void listener(ListSupParameterDto listParameterDto) {
        List<SupParameterDto<?>> list = listParameterDto.getList();
        list.stream().forEach(parameterService::addParam);
    }
}
