package ru.shilaev.investorchestrator.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import ru.shilaev.investorchestrator.dto.model.InstrumentHistoricCandleDto;
import ru.shilaev.investorchestrator.service.analytics.AnalyticsServiceProxy;

import java.io.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final AnalyticsServiceProxy mathService;

    // Метод для генерации Excel файла с историческими свечами
    @SneakyThrows
    public ByteArrayResource generateHistoricCandlesExcelFile(String sheetLabel,
                                                              Instant from,
                                                              Instant to,
                                                              List<InstrumentHistoricCandleDto> historicCandleDtoList) {
        // Конфигурационные константы
        final int COLUMN_WIDTH = 20 * 400;
        final int[] COLUMNS = {0, 1, 2, 3, 4};
        final String[] HEADERS = {"Время", "Открытие", "Максимальная", "Минимальная", "Закрытие"};

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet(sheetLabel);

            // Стили для ячеек
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
            dateStyle.setAlignment(HorizontalAlignment.LEFT);

            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
            numberStyle.setAlignment(HorizontalAlignment.LEFT);

            // Настройка ширины столбцов
            for (int col : COLUMNS) {
                sheet.setColumnWidth(col, COLUMN_WIDTH);
            }

            // Информация о выборке
            Row titleRow = sheet.createRow(0);
            addCell(titleRow, 0, sheetLabel, textStyle);

            // Блок информации о датах
            Row infoRow = sheet.createRow(1);
            addCell(infoRow, 0, "Начиная с:", textStyle);
            addCell(infoRow, 1, Date.from(from), dateStyle);

            addCell(infoRow, 2, "Заканчивая:", textStyle);
            addCell(infoRow, 3, Date.from(to), dateStyle);

            // Заголовки таблицы
            Row headerRow = sheet.createRow(2);
            for (int i = 0; i < HEADERS.length; i++) {
                headerRow.createCell(i).setCellValue(HEADERS[i]);
            }

            // Основные данные
            List<BigDecimal> opens = new ArrayList<>();
            List<BigDecimal> closes = new ArrayList<>();
            List<BigDecimal> highs = new ArrayList<>();
            List<BigDecimal> lows = new ArrayList<>();

            for (int i = 0; i < historicCandleDtoList.size(); i++) {
                InstrumentHistoricCandleDto candle = historicCandleDtoList.get(i);
                Row row = sheet.createRow(i + 3);

                // Дата
                addCell(row, 0, Date.from(candle.time()), dateStyle);

                // Цены
                addCell(row, 1, candle.openPrice().doubleValue(), numberStyle);
                opens.add(candle.openPrice());
                addCell(row, 2, candle.highPrice().doubleValue(), numberStyle);
                highs.add(candle.highPrice());
                addCell(row, 3, candle.lowPrice().doubleValue(), numberStyle);
                lows.add(candle.lowPrice());
                addCell(row, 4, candle.closePrice().doubleValue(), numberStyle);
                closes.add(candle.closePrice());
            }

            // Мат. ожидание
            if (!historicCandleDtoList.isEmpty()) {
                int lastRow = historicCandleDtoList.size() + 3;
                Row mathExpectationRow = sheet.createRow(lastRow);

                mathExpectationRow.createCell(0).setCellValue("Мат. ожидание");
                addCell(mathExpectationRow, 0, "Мат. ожидание", textStyle);
                addCell(mathExpectationRow, 1, mathService.getMathExpectation(opens).getResult(), numberStyle);
                addCell(mathExpectationRow, 2, mathService.getMathExpectation(highs).getResult(), numberStyle);
                addCell(mathExpectationRow, 3, mathService.getMathExpectation(lows).getResult(), numberStyle);
                addCell(mathExpectationRow, 4, mathService.getMathExpectation(closes).getResult(), numberStyle);

            }

            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }


    private void addCell(Row row, int col, Double value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void addCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void addCell(Row row, int col, Date value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

}