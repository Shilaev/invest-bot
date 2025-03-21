package ru.shilaev.investorchestrator.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Table(name = "instrument_historic_candle", schema = "analytics_data")
@Data @NoArgsConstructor @AllArgsConstructor
public class InstrumentHistoricCandle {

    @Id
    private UUID id;               // Уникальный идентификатор (UUID)
    private String instrumentId;   // Идентификатор инструмента
    private Instant time;          // Время, когда была зафиксирована свеча
    private String candleInterval; // Интервал свечи
    private String candleSource;   // Источник данных для свечи
    private BigDecimal openPrice;  // Цена открытия
    private BigDecimal closePrice; // Цена закрытия
    private BigDecimal highPrice;  // Максимальная цена
    private BigDecimal lowPrice;   // Минимальная цена
}