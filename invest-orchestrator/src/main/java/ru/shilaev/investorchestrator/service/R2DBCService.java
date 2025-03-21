package ru.shilaev.investorchestrator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shilaev.investorchestrator.data.model.InstrumentHistoricCandle;
import ru.shilaev.investorchestrator.dto.model.InstrumentHistoricCandleDto;

import java.time.Instant;
import java.util.Comparator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class R2DBCService {

    // Шаблон для работы с R2DBC
    private final R2dbcEntityTemplate template;

    // Метод для вставки исторической свечи в базу данных
    public Mono<InstrumentHistoricCandle> insertHistoricCandle(InstrumentHistoricCandleDto instrumentHistoricCandleDto) {
        // Создание объекта свечи для сохранения
        InstrumentHistoricCandle candleToSave = new InstrumentHistoricCandle(
                UUID.randomUUID(),                            // Генерация уникального идентификатора
                instrumentHistoricCandleDto.instrumentId(),   // Идентификатор инструмента
                instrumentHistoricCandleDto.time(),           // Время свечи
                instrumentHistoricCandleDto.candleInterval(), // Интервал свечи
                instrumentHistoricCandleDto.candleSource(),   // Источник данных
                instrumentHistoricCandleDto.openPrice(),      // Цена открытия
                instrumentHistoricCandleDto.closePrice(),     // Цена закрытия
                instrumentHistoricCandleDto.highPrice(),      // Максимальная цена
                instrumentHistoricCandleDto.lowPrice()        // Минимальная цена
        );

        // Вставка свечи в базу данных и обработка ошибок
        return template.insert(InstrumentHistoricCandle.class)
                .using(candleToSave) // Использование созданного объекта свечи
                .onErrorMap(e -> new RuntimeException("Error saving candle: " + e.getMessage())); // Обработка ошибок
    }

    // Метод для поиска всех исторических свечей по идентификатору инструмента и диапазону дат
    public Flux<InstrumentHistoricCandleDto> findAllByInstrumentId(String instrumentId,
                                                                   Instant fromDate, Instant toDate,
                                                                   String candleInterval) {
        // Создание запроса для выборки свечей из базы данных
        return template.select(InstrumentHistoricCandle.class)
                .matching(Query.query(
                        Criteria.where("instrument_Id").is(instrumentId)   // Фильтр по идентификатору инструмента
                                .and("candle_interval").is(candleInterval) // Фильтр по интервалу свечи
                                .and("time").greaterThanOrEquals(fromDate) // Фильтр по времени (больше или равно)
                                .and("time").lessThanOrEquals(toDate)      // Фильтр по времени (меньше или равно)
                )).all() // Получение всех подходящих свечей
                .map(candle -> new InstrumentHistoricCandleDto(
                        candle.getInstrumentId(), // Преобразование в DTO
                        candle.getTime(),
                        candle.getCandleInterval(),
                        candle.getCandleSource(),
                        candle.getOpenPrice(),
                        candle.getClosePrice(),
                        candle.getHighPrice(),
                        candle.getLowPrice()
                ))
                .sort(Comparator.comparing(InstrumentHistoricCandleDto::time)) // Сортировка по времени
                .onErrorMap(e -> new RuntimeException("Error finding all candles for instrumentId: " + instrumentId)); // Обработка ошибок
    }

}