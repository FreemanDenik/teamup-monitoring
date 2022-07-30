package ru.team.up.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan("ru.team.up.core")
@EnableMongoRepositories("ru.team.up.core.repositories")
@PropertySource({
        "classpath:db.properties",
        "classpath:sup.properties",
})
@EnableWebMvc
public class TeamupMonitoringApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamupMonitoringApplication.class, args);
    }
}
