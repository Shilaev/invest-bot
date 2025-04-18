package ru.shilaev.investorchestrator.service;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class ConvertService {

    //Метод для преобразования Instant в Timestamp
    public Timestamp convertInstantToTimestamp(Instant time) {
        return Timestamp.newBuilder()
                .setSeconds(time.getEpochSecond()) // Установка секунд
                .setNanos(time.getNano())          // Установка наносекунд
                .build();                          // Создание объекта Timestamp
    }

    //Метод для преобразования Timestamp в Instant
    public Instant convertTimestampToInstant(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()); // Создание объекта Instant
    }

    //Метод для преобразования Quotation в BigDecimal
    public BigDecimal convertQuotationToBigDecimal(Quotation quotation) {
        // Проверка на нулевое значение и преобразование в BigDecimal
        return quotation.getUnits() == 0 && quotation.getNano() == 0 ? BigDecimal.ZERO :
                BigDecimal.valueOf(quotation.getUnits()).add(BigDecimal.valueOf(quotation.getNano(), 9)); // Преобразование
    }

    //Преобразует currency код в наименование валюты
    public String convertCurrencyCodeToString(String currencyCode) {
        return switch (currencyCode) {
            case "643" -> "Рубль";
            case "840" -> "Доллар США";
            case "978" -> "Евро";
            case "392" -> "Японская иена";
            case "826" -> "Фунт стерлингов";
            case "156" -> "Китайский юань";
            case null -> "null";
            default -> "Unknown";
        };
    }
}