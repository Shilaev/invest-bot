package ru.shilaev.investorchestrator.dto.controller;

import java.time.Instant;

public record GenerateHistoricCandlesXLSXRequestDto(
        String fileName,      // Наименование файла
        String sheetLabel,    // Наименование листа
        Instant fromDate,     // Начало запрашиваемого периода по UTC.
        Instant toDate,       // Окончание запрашиваемого периода по UTC.
        String instrumentId,  // Идентификатор инструмента. Принимает значение figi или instrument_uid.
        String candleInterval // Интервал запрошенных свечей.
) {
}
