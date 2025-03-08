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
    private String address;

    @Bean(name = "analyticsApiManagedChannel")
    ManagedChannel analyticsApiManagedChannel() {
        return ManagedChannelBuilder.forTarget("localhost:" + address)
                .maxInboundMessageSize(16 * 1024 * 1024)
                .usePlaintext()
                .build();
    }

    @Bean
    DataProcessingGrpc.DataProcessingBlockingStub dataProcessingBlockingStub(ManagedChannel analyticsApiManagedChannel) {
        return DataProcessingGrpc.newBlockingStub(analyticsApiManagedChannel);
    }

}
