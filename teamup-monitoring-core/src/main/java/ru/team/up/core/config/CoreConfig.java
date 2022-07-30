package ru.team.up.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.team.up.core.tasks.MessageListener;

@Configuration
public class CoreConfig {
    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }
}
