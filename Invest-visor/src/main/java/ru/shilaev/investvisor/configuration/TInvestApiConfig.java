package ru.shilaev.investvisor.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.security.BearerTokenAuthenticationInterceptor;
import ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc;
import ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc.InstrumentsServiceBlockingStub;
import ru.tinkoff.piapi.contract.v1.MarketDataServiceGrpc;
import ru.tinkoff.piapi.contract.v1.MarketDataServiceGrpc.MarketDataServiceBlockingStub;

@Configuration @Getter
public class TInvestApiConfig {

    @Value("${tinvest.sandbox.host}")
    private String host; // Хост для подключения к API Tinkoff Invest

    @Value("${tinvest.sandbox.token}")
    private String token; // Токен для аутентификации в API Tinkoff Invest

    // Метод для создания и настройки gRPC канала
    @Bean ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forTarget(this.host) // Установка целевого хоста
                .useTransportSecurity() // Использование защищенного транспорта
                .intercept(new BearerTokenAuthenticationInterceptor(this.token)) // Добавление интерсептора для аутентификации
                .build(); // Создание канала
    }

    // Метод для создания блокирующего клиента для работы с сервисом инструментов
    @Bean InstrumentsServiceBlockingStub instrumentsServiceBlockingStub(ManagedChannel managedChannel) {
        return InstrumentsServiceGrpc.newBlockingStub(managedChannel); // Создание клиента с использованием канала
    }

    // Метод для создания блокирующего клиента для работы с сервисом рыночных данных
    @Bean MarketDataServiceBlockingStub marketDataServiceBlockingStub(ManagedChannel managedChannel) {
        return MarketDataServiceGrpc.newBlockingStub(managedChannel); // Создание клиента с использованием канала
    }

}