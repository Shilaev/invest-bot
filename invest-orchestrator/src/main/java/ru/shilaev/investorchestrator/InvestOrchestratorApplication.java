package ru.shilaev.investorchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.io.IOException;

@SpringBootApplication
@EnableR2dbcRepositories
public class InvestOrchestratorApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(InvestOrchestratorApplication.class, args);

    }


}