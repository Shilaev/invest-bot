package ru.shilaev.investorchestrator.service.analytics.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.shilaev.invest_bot.AnalyticsApi;
import ru.shilaev.invest_bot.DataProcessingGrpc.DataProcessingBlockingStub;
import ru.shilaev.investorchestrator.service.analytics.AnalyticsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GolangAnalyticsService implements AnalyticsService {
    private final DataProcessingBlockingStub dataProcessingBlockingStub;

    public GolangAnalyticsService(@Qualifier("golangDataProcessingBlockingStub")
                                  DataProcessingBlockingStub dataProcessingBlockingStub) {
        this.dataProcessingBlockingStub = dataProcessingBlockingStub;
    }

    @Override public AnalyticsApi.AnalyticsResult getMathExpectation(List<BigDecimal> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("Input numbers cannot be null or empty");
        }

        AnalyticsApi.NumberArray numberArray = AnalyticsApi.NumberArray.newBuilder()
                .addAllNumbers(numbers.stream()
                        .map(BigDecimal::doubleValue) // Преобразуем BigDecimal в Double
                        .collect(Collectors.toList())) // Собираем в список
                .build();

        try {
            return dataProcessingBlockingStub.processNumbers(numberArray);
        } catch (Exception e) {
            throw new RuntimeException("Error processing numbers", e);
        }
    }
}
