package ru.shilaev.investvisor.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.shilaev.investvisor.DataProcessingGrpc;

import java.io.IOException;

@Configuration @Getter
public class AnalyticsApiConfig {

    @Value("${analytics.server.port}")
    private int analyticsServerPort;


    @Bean(name = "analyticsApiServer")
    Server analyticsApiServer(DataProcessingGrpc.DataProcessingImplBase dataProcessingImplBase) throws IOException {
        return ServerBuilder.forPort(this.analyticsServerPort)
                .addService(dataProcessingImplBase)
                .build()
                .start();
    }

}
