package ru.shilaev.investvisor.dto.model;

import java.math.BigDecimal;
import java.time.Instant;

public record InstrumentHistoricCandleDto(
        String instrumentId,    // Идентификатор инструмента. Принимает значение figi или instrument_uid.
        Instant time,           // Время, когда была зафиксирована свеча — последняя closePrice (в формате UTC).
        String candleInterval,  // Интервал свечи (например, "1m" для одной минуты, "1h" для одного часа и т.д.).
        String candleSource,    // Источник данных для свечи (например, "exchange", "api" и т.д.).
        BigDecimal openPrice,   // Цена открытия свечи.
        BigDecimal closePrice,  // Цена закрытия свечи.
        BigDecimal highPrice,   // Максимальная цена за период свечи.
        BigDecimal lowPrice     // Минимальная цена за период свечи.
) {
    public String toMinimalInfoString() {
        return "==========================================" + "\n" +
                "Time: " + time + "\n" +
                "Open price: " + openPrice + "\n" +
                "Close price: " + closePrice + "\n" +
                "High price: " + highPrice + "\n" +
                "Low price: " + lowPrice + "\n" +
                "==========================================" + "\n";
    }
}
