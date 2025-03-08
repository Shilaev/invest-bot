package ru.shilaev.investvisor.api;

// Импорт необходимых библиотек и классов

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shilaev.investvisor.dto.controller.FindInstrumentRequestDto;
import ru.shilaev.investvisor.dto.controller.GenerateHistoricCandlesXLSXRequestDto;
import ru.shilaev.investvisor.dto.controller.GetHistoricCandlesRequestDto;
import ru.shilaev.investvisor.dto.model.InstrumentHistoricCandleDto;
import ru.shilaev.investvisor.service.*;
import ru.tinkoff.piapi.contract.v1.*;

import java.util.ArrayList;

@RestController
@RequestMapping("statistics/api")
@RequiredArgsConstructor
public class StatisticsController {

    // Сервисы, используемые в контроллере
    private final TInvestService tInvestService;
    private final UtilsService utilsService;
    private final ExcelService excelService;
    private final R2DBCService r2DBCService;

    // Метод для поиска инструментов по запросу
    @PostMapping(value = "/find-instrument")
    private Flux<String> findInstruments(@Valid @RequestBody FindInstrumentRequestDto findInstrumentRequestDto) {
        // Создание запроса на поиск инструмента
        var findInstrumentRequest = FindInstrumentRequest.newBuilder()
                .setQuery(findInstrumentRequestDto.query())
                .setInstrumentKind(InstrumentType.valueOf(findInstrumentRequestDto.instrumentType()))
                .setApiTradeAvailableFlag(findInstrumentRequestDto.apiTradeAvailableFlag())
                .build();

        // Получение ответа от сервиса с найденными инструментами
        FindInstrumentResponse instruments = tInvestService.findInstrument(findInstrumentRequest);

        // Преобразование списка инструментов в Flux строк
        return Flux.fromIterable(instruments.getInstrumentsList())
                .map(instrument -> {
                    // Форматирование информации об инструменте в строку
                    String stringBuilder = "==========================================" + "\n" +
                            "NAME: " + instrument.getName() + "\n" +
                            "ISIN: " + instrument.getIsin() + "\n" +
                            "FIGI: " + instrument.getFigi() + "\n" +
                            "TYPE: " + instrument.getInstrumentType() + "\n" +
                            "LOT: " + instrument.getLot() + "\n" +
                            "UID: " + instrument.getUid() + "\n" +
                            "==========================================" + "\n" +
                            "\n";
                    return stringBuilder;
                });
    }

    // Метод для получения исторических свечей инструмента
    @PostMapping(value = "/get-historic-candles")
    private Flux<String> getInstrumentHistoricCandles(@Valid @RequestBody GetHistoricCandlesRequestDto requestDto) {
        // Создание запроса на получение исторических свечей
        var getCandlesRequest = GetCandlesRequest.newBuilder()
                .setInstrumentId(requestDto.instrumentId())
                .setFrom(utilsService.convertInstantToTimestamp(requestDto.fromDate()))
                .setTo(utilsService.convertInstantToTimestamp(requestDto.toDate()))
                .setInterval(CandleInterval.valueOf(requestDto.candleInterval()))
                .setCandleSourceType(GetCandlesRequest.CandleSource.valueOf(requestDto.candleSource()))
                .setLimit(requestDto.limit())
                .build();

        // Получение сырых данных свечей от сервиса
        var rawCandles = tInvestService.getInstrumentHistoricalCandles(getCandlesRequest);
        // Преобразование сырых данных в список объектов свечей
        ArrayList<InstrumentHistoricCandleDto> candles = tInvestService.mapRawHistoricalCandles(
                requestDto.instrumentId(),
                requestDto.candleInterval(),
                requestDto.candleSource(),
                rawCandles.getCandlesList()
        );

        // Проверка на пустой список свечей
        if (candles == null) return Flux.just("Empty candle list");
        // Возвращение количества свечей и их информации
        return Flux.just("Total candles: " + candles.size() + "\n")
                .concatWith(Flux.fromIterable(candles)
                        .flatMap(candleDto -> r2DBCService.insertHistoricCandle(candleDto)
                                .map(savedCandle -> candleDto.toMinimalInfoString())
                        )
                );
    }

    // Метод для скачивания исторических свечей в формате XLSX
    @PostMapping(value = "/download-historic-candles")
    private Mono<ResponseEntity<ByteArrayResource>> downloadHistoricCandles(
            @Valid @RequestBody GenerateHistoricCandlesXLSXRequestDto requestDto) {
        // Поиск всех исторических свечей по заданным параметрам
        return r2DBCService.findAllByInstrumentId(requestDto.instrumentId(),
                        requestDto.fromDate(), requestDto.toDate(),
                        requestDto.candleInterval())
                .collectList() // Сбор всех свечей в список
                .map(ArrayList::new) // Преобразование в ArrayList
                .flatMap(candles -> {
                    // Генерация Excel файла с историческими свечами
                    ByteArrayResource resource = excelService.generateHistoricCandlesExcelFile(
                            requestDto.sheetLabel(),
                            requestDto.fromDate(),
                            requestDto.toDate(),
                            candles
                    );

                    // Возвращение ответа с файлом для скачивания
                    return Mono.just(ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                                    requestDto.fileName() + ".xlsx\"")
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(resource));
                });
    }

}