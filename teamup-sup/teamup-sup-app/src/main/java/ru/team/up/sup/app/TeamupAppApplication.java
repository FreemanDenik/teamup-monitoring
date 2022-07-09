package ru.team.up.sup.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan({"ru.team.up.sup"})
@EnableJpaRepositories("ru.team.up.sup.core.repositories")
@EntityScan("ru.team.up.sup.core")
@PropertySource({
        "classpath:db.properties",
        "classpath:auth.properties",
        "classpath:kafka-sup.properties"
})
@EnableWebMvc
public class TeamupAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamupAppApplication.class, args);
    }

}
