package ru.team.up.sup.input;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"ru.team.up.sup.input", "ru.team.up.sup.core"})
public class TeamupInputApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamupInputApplication.class, args);
    }
}
