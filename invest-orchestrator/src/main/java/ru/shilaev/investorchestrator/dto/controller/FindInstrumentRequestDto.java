package ru.shilaev.investorchestrator.dto.controller;

public record FindInstrumentRequestDto(
        String query,                 // Строка поиска
        String instrumentType,        // Фильтр по типу инструмента
        boolean apiTradeAvailableFlag // Фильтр для отображения только торговых инструментов
) {
}
