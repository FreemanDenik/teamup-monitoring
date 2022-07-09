package ru.team.up.sup.core.service.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.team.up.dto.AppModuleNameDto;
import ru.team.up.sup.core.service.KafkaSupService;
import ru.team.up.sup.core.service.ParameterService;

import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaMessageListener {

    private KafkaSupService kafkaSupService;
    private ParameterService parameterService;

    @KafkaListener(topics = "${kafka.topic.name}", containerFactory = "kafkaModuleContainerFactory")
    public void listenForModuleParameterRequests(AppModuleNameDto module) {
        log.debug("KafkaListener: получено сообщение c запросом настроек модуля");
        if (module == null) {
            log.debug("KafkaListener: модуль запрашивающий настройки = null");
            throw new RuntimeException("Модуль запрашивающий настройки = null");
        }
        kafkaSupService.send(parameterService.getParametersBySystemName(module).stream()
                .filter(p -> p.getInUse())
                .collect(Collectors.toList()));
        log.debug("KafkaListener: настройки для модуля {} отправлены", module);
    }
}