package ru.team.up.teamup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import ru.team.up.teamup.tasks.MessageListener;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class TeamupMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamupMonitoringApplication.class, args);
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }
}
