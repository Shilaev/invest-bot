package ru.shilaev.investvisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import ru.shilaev.investvisor.service.MathematicsService;

import java.io.IOException;

@SpringBootApplication
@EnableR2dbcRepositories
public class InvestVisorApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(InvestVisorApplication.class, args);

    }


}