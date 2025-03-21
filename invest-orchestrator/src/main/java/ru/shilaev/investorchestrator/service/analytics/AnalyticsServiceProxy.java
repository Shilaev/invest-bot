package ru.shilaev.investorchestrator.service.analytics;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.shilaev.invest_bot.AnalyticsApi;
import ru.shilaev.invest_bot.AnalyticsApi.AnalyticsResult;
import ru.shilaev.investorchestrator.configuration.AnalyticsApiConfig;
import ru.shilaev.investorchestrator.service.analytics.impl.GolangAnalyticsService;
import ru.shilaev.investorchestrator.service.analytics.impl.PythonAnalyticsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

@Primary @Service
@RequiredArgsConstructor
public class AnalyticsServiceProxy implements AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsServiceProxy.class);
    private final PythonAnalyticsService pythonAnalyticsService;
    private final GolangAnalyticsService golangAnalyticsService;
    private final AnalyticsApiConfig analyticsApiConfig;

    @Override
    public AnalyticsApi.AnalyticsResult getMathExpectation(List<BigDecimal> numbers) {
        String method = analyticsApiConfig.getMethods().get("getMathExpectation");
        Supplier<AnalyticsApi.AnalyticsResult> primaryService = method.equals("golang") ?
                () -> golangAnalyticsService.getMathExpectation(numbers) :
                () -> pythonAnalyticsService.getMathExpectation(numbers);

        Supplier<AnalyticsApi.AnalyticsResult> fallbackService = method.equals("golang") ?
                () -> pythonAnalyticsService.getMathExpectation(numbers) :
                () -> golangAnalyticsService.getMathExpectation(numbers);

        return executeWithFallBack(primaryService, fallbackService);
    }

    private AnalyticsResult executeWithFallBack(Supplier<AnalyticsResult> primarySupplier,
                                                Supplier<AnalyticsResult> fallbackSupplier) {
        try {
            return primarySupplier.get();
        } catch (Exception e) {
            log.info("Primary supplier failed, trying fallback supplier", e);
            return fallbackSupplier.get();
        }

    }

}
