package ru.shilaev.investorchestrator.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.security.BearerTokenAuthenticationInterceptor;
import ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc;
import ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc.InstrumentsServiceBlockingStub;
import ru.tinkoff.piapi.contract.v1.MarketDataServiceGrpc;
import ru.tinkoff.piapi.contract.v1.MarketDataServiceGrpc.MarketDataServiceBlockingStub;
import ru.tinkoff.piapi.contract.v1.SandboxServiceGrpc;
import ru.tinkoff.piapi.contract.v1.SandboxServiceGrpc.SandboxServiceBlockingStub;

@Configuration @Getter
public class TInvestApiConfig {

    @Value("${tinvest.sandbox.host}")
    private String host; // Хост для подключения к API Tinkoff Invest

    @Value("${tinvest.sandbox.token}")
    private String token; // Токен для аутентификации в API Tinkoff Invest

    // Метод для создания и настройки gRPC канала
    @Bean(name = "tInvestApiManagedChannel")
    ManagedChannel tInvestApiManagedChannel() {
        return ManagedChannelBuilder.forTarget(this.host) // Установка целевого хоста
                .useTransportSecurity() // Использование защищенного транспорта
                .intercept(new BearerTokenAuthenticationInterceptor(this.token)) // Добавление интерсептора для аутентификации
                .build(); // Создание канала
    }

    // Метод для создания блокирующего клиента для работы с сервисом инструментов
    @Bean InstrumentsServiceBlockingStub instrumentsServiceBlockingStub(@Qualifier("tInvestApiManagedChannel") ManagedChannel tInvestApiManagedChannel) {
        return InstrumentsServiceGrpc.newBlockingStub(tInvestApiManagedChannel); // Создание клиента с использованием канала
    }

    // Метод для создания блокирующего клиента для работы с сервисом рыночных данных
    @Bean MarketDataServiceBlockingStub marketDataServiceBlockingStub(@Qualifier("tInvestApiManagedChannel") ManagedChannel tInvestApiManagedChannel) {
        return MarketDataServiceGrpc.newBlockingStub(tInvestApiManagedChannel); // Создание клиента с использованием канала
    }

    @Bean SandboxServiceBlockingStub sandboxServiceBlockingStub(@Qualifier("tInvestApiManagedChannel") ManagedChannel tInvestApiManagedChannel) {
        return SandboxServiceGrpc.newBlockingStub(tInvestApiManagedChannel);
    }
}