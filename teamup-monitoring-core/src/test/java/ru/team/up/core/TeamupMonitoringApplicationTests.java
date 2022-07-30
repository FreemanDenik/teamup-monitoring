package ru.team.up.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeamupMonitoringApplicationTests {

    public static void main(String[] args) {
        try {
            SpringApplication.run(TeamupMonitoringApplicationTests.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
