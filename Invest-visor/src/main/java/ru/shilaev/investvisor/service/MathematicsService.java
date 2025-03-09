package ru.shilaev.investvisor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shilaev.invest_bot.AnalyticsApi;
import ru.shilaev.invest_bot.AnalyticsApi.NumberArray;
import ru.shilaev.invest_bot.DataProcessingGrpc;
import ru.shilaev.invest_bot.DataProcessingGrpc.DataProcessingBlockingStub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MathematicsService {

    private final DataProcessingBlockingStub dataProcessingBlockingStub;

    public AnalyticsApi.AnalyticsResult getMathExpectation(List<BigDecimal> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("Input numbers cannot be null or empty");
        }

        NumberArray numberArray = NumberArray.newBuilder()
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
