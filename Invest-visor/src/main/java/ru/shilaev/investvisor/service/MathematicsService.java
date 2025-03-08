package ru.shilaev.investvisor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shilaev.investvisor.AnalyticsApi.AnalyticsResult;
import ru.shilaev.investvisor.AnalyticsApi.NumberArray;
import ru.shilaev.investvisor.DataProcessingGrpc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MathematicsService {

    private final DataProcessingGrpc.DataProcessingBlockingStub dataProcessingBlockingStub;

    public AnalyticsResult getMathExpectation(ArrayList<BigDecimal> numbers) {
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
