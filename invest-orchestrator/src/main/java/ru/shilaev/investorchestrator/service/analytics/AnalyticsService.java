package ru.shilaev.investorchestrator.service.analytics;

import ru.shilaev.invest_bot.AnalyticsApi;

import java.math.BigDecimal;
import java.util.List;

public interface AnalyticsService {

    AnalyticsApi.AnalyticsResult getMathExpectation(List<BigDecimal> numbers);

}
