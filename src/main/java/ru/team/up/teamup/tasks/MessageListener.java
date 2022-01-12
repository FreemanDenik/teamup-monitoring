package ru.team.up.teamup.tasks;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import ru.team.up.teamup.entity.Report;
import ru.team.up.teamup.service.DataService;

public class MessageListener {

    @Autowired
    private DataService dataService;

    @KafkaListener(topics = "${kafka.topic.name}", containerFactory = "kafkaListenerContainerFactory")
    public void listener(Report data) {
        System.out.println("Received message: " + data);
        dataService.saveMessage(data);
    }
}
