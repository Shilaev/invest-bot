package ru.shilaev.investvisor.dto.controller;

import java.time.Instant;

public record GetHistoricCandlesRequestDto(
        String instrumentId,   // Идентификатор инструмента. Принимает значение figi или instrument_uid.
        Instant fromDate,      // Начало запрашиваемого периода по UTC.
        Instant toDate,        // Окончание запрашиваемого периода по UTC.
        String candleInterval, // Интервал запрошенных свечей.
        String candleSource,   // Тип источника свечи.
        int limit              // Максимальное количество свечей в ответе.
) {
}
