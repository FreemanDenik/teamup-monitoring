package ru.team.up.core.tasks;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import ru.team.up.core.entity.Report;
import ru.team.up.core.service.DataService;

@Slf4j
public class MessageListener {

    @Autowired
    private DataService dataService;

    @KafkaListener(topics = "${kafka.topic.name}", containerFactory = "kafkaListenerContainerFactory")
    public void listener(Report data) {
        log.debug("Получено сообщение: " + data);
        dataService.saveMessage(data);
    }
}
