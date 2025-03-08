package ru.shilaev.investvisor;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import ru.shilaev.investvisor.service.DataProcessing;

import java.io.IOException;

@SpringBootApplication
@EnableR2dbcRepositories
public class InvestVisorApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(InvestVisorApplication.class, args);


        Server server = ServerBuilder.forPort(50051)
                .addService(new DataProcessing())
                .build()
                .start();

    }



}