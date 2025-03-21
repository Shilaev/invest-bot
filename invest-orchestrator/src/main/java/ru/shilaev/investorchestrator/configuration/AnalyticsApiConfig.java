package ru.shilaev.investorchestrator.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.shilaev.invest_bot.DataProcessingGrpc;

import java.util.Map;

/**
 * Создаются 2 канала, один для Python приложения, другой для Golang приложения
 * Это нужно, чтобы была возможность переезда методов из Python в Golang, если
 * вдруг Python не будет справляться с нагрузкой
 */
@Configuration @Getter @Setter
@ConfigurationProperties(prefix = "analytics.provider")
public class AnalyticsApiConfig {

    // Каждый метод может иметь разную реализацию, либо Python, либо Golang
    // Поэтому все запросы будут происходить через Прокси сервис, который
    // считывает методы из properties.yml и запускает их в правильной последовательнсоти
    private Map<String, String> methods;

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
