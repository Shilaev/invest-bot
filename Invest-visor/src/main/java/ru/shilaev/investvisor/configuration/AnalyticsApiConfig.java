package ru.shilaev.investvisor.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.shilaev.investvisor.DataProcessingGrpc;

@Configuration @Getter
public class AnalyticsApiConfig {

    @Value("${analytics_api.host}")
    private String address;

    @Bean(name = "analyticsApiManagedChannel")
    ManagedChannel analyticsApiManagedChannel() {
        return ManagedChannelBuilder.forTarget(address)
                .maxInboundMessageSize(16 * 1024 * 1024)
                .usePlaintext()
                .build();
    }

    @Bean
    DataProcessingGrpc.DataProcessingBlockingStub dataProcessingBlockingStub(ManagedChannel analyticsApiManagedChannel) {
        return DataProcessingGrpc.newBlockingStub(analyticsApiManagedChannel);
    }

}
