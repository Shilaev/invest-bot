package ru.shilaev.investvisor.service;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class UtilsService {

    // Метод для преобразования Instant в Timestamp
    public Timestamp convertInstantToTimestamp(Instant time) {
        return Timestamp.newBuilder()
                .setSeconds(time.getEpochSecond()) // Установка секунд
                .setNanos(time.getNano())          // Установка наносекунд
                .build();                          // Создание объекта Timestamp
    }

    // Метод для преобразования Timestamp в Instant
    public Instant convertTimestampToInstant(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()); // Создание объекта Instant
    }

    // Метод для преобразования Quotation в BigDecimal
    public BigDecimal convertQuotationToBigDecimal(Quotation quotation) {
        // Проверка на нулевое значение и преобразование в BigDecimal
        return quotation.getUnits() == 0 && quotation.getNano() == 0 ? BigDecimal.ZERO :
                BigDecimal.valueOf(quotation.getUnits()).add(BigDecimal.valueOf(quotation.getNano(), 9)); // Преобразование
    }

}