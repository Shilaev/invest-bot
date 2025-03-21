package ru.shilaev.investorchestrator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shilaev.investorchestrator.dto.model.InstrumentHistoricCandleDto;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc.InstrumentsServiceBlockingStub;
import ru.tinkoff.piapi.contract.v1.MarketDataServiceGrpc.MarketDataServiceBlockingStub;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TInvestService {

    // Блокирующие клиенты для работы с сервисами инструментов и рыночных данных
    private final InstrumentsServiceBlockingStub instrumentsServiceBlockingStub;
    private final MarketDataServiceBlockingStub marketDataServiceBlockingStub;
    private final UtilsService utilsService; // Сервис для утилитных функций

    // Метод для поиска инструмента по запросу
    public FindInstrumentResponse findInstrument(FindInstrumentRequest request) {
        return instrumentsServiceBlockingStub.findInstrument(request); // Вызов метода поиска инструмента
    }

    // Метод для получения консенсус-прогнозов
    public GetConsensusForecastsResponse getConsensusForecasts(GetConsensusForecastsRequest request) {
        return instrumentsServiceBlockingStub.getConsensusForecasts(request); // Вызов метода получения прогнозов
    }

    // Метод для получения исторических свечей инструмента
    public GetCandlesResponse getInstrumentHistoricalCandles(GetCandlesRequest request) {
        return marketDataServiceBlockingStub.getCandles(request); // Вызов метода получения исторических свечей
    }

    // Метод для преобразования сырых исторических свечей в форматированный список DTO
    public ArrayList<InstrumentHistoricCandleDto> mapRawHistoricalCandles(
            String instrumentId, String candleInterval, String candleSource, List<HistoricCandle> rawHistoricalCandles) {
        // Проверка на пустой список свечей
        if (rawHistoricalCandles.isEmpty()) return null;

        ArrayList<InstrumentHistoricCandleDto> formatedCandles = new ArrayList<>(); // Список для форматированных свечей

        // Преобразование каждой свечи в DTO
        for (HistoricCandle candle : rawHistoricalCandles) {
            formatedCandles.add(new InstrumentHistoricCandleDto(
                    instrumentId, // Идентификатор инструмента
                    utilsService.convertTimestampToInstant(candle.getTime()), // Время свечи
                    candleInterval, // Интервал свечи
                    candleSource, // Источник данных
                    utilsService.convertQuotationToBigDecimal(candle.getOpen()),  // Цена открытия
                    utilsService.convertQuotationToBigDecimal(candle.getClose()), // Цена закрытия
                    utilsService.convertQuotationToBigDecimal(candle.getHigh()),  // Максимальная цена
                    utilsService.convertQuotationToBigDecimal(candle.getLow())    // Минимальная цена
            ));
        }

        return formatedCandles; // Возврат списка форматированных свечей
    }

}