package ru.shilaev.investvisor.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.shilaev.invest_bot.DataProcessingGrpc;

/**
 * Создаются 2 канала, один для Python приложения, другой для Golang приложения
 * Это нужно, чтобы была возможность переезда методов из Python в Golang, если
 * вдруг Python не будет справляться с нагрузкой
 */
@Configuration @Getter
public class AnalyticsApiConfig {
    @Value("${analytics.server.python.port}")
    private String pythonServerPort;

    @Value("${analytics.server.golang.port}")
    private String golangServerPort;

    @Bean(name = "pythonAnalyticsApiManagedChannel")
    ManagedChannel pythonAnalyticsApiManagedChannel() {
        return ManagedChannelBuilder.forTarget("localhost:" + pythonServerPort)
                .maxInboundMessageSize(16 * 1024 * 1024)
                .usePlaintext()
                .build();
    }

    @Bean(name = "golangAnalyticsApiManagedChannel")
    ManagedChannel golangAnalyticsApiManagedChannel() {
        return ManagedChannelBuilder.forTarget("localhost:" + golangServerPort)
                .maxInboundMessageSize(16 * 1024 * 1024)
                .usePlaintext()
                .build();
    }

    @Bean
    DataProcessingGrpc.DataProcessingBlockingStub pythonDataProcessingBlockingStub(ManagedChannel pythonAnalyticsApiManagedChannel) {
        return DataProcessingGrpc.newBlockingStub(pythonAnalyticsApiManagedChannel);
    }

    @Bean
    DataProcessingGrpc.DataProcessingBlockingStub golangDataProcessingBlockingStub(ManagedChannel golangAnalyticsApiManagedChannel) {
        return DataProcessingGrpc.newBlockingStub(golangAnalyticsApiManagedChannel);
    }

}
