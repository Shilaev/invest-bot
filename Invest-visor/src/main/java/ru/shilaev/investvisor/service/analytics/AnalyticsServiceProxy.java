package ru.shilaev.investvisor.service.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.shilaev.invest_bot.AnalyticsApi;
import ru.shilaev.investvisor.configuration.AnalyticsApiConfig;
import ru.shilaev.investvisor.service.analytics.impl.GolangAnalyticsService;
import ru.shilaev.investvisor.service.analytics.impl.PythonAnalyticsService;

import java.math.BigDecimal;
import java.util.List;

@Primary @Service
@RequiredArgsConstructor
public class AnalyticsServiceProxy implements AnalyticsService {

    private final PythonAnalyticsService pythonAnalyticsService;
    private final GolangAnalyticsService golangAnalyticsService;
    private final AnalyticsApiConfig analyticsApiConfig;

    @Override
    public AnalyticsApi.AnalyticsResult getMathExpectation(List<BigDecimal> numbers) {
        return analyticsApiConfig.getMethods().get("getMathExpectation").equals("golang") ?
                golangAnalyticsService.getMathExpectation(numbers) :
                pythonAnalyticsService.getMathExpectation(numbers);
    }


}
