package ru.shilaev.investvisor.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import ru.shilaev.investvisor.dto.model.InstrumentHistoricCandleDto;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final MathematicsService mathService;

    // Метод для генерации Excel файла с историческими свечами
    @SneakyThrows
    public ByteArrayResource generateHistoricCandlesExcelFile(String sheetLabel,
                                                              Instant from,
                                                              Instant to,
                                                              ArrayList<InstrumentHistoricCandleDto> historicCandleDtoList) {
        // Создание нового рабочего файла Excel
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); // Поток для записи данных в массив байтов

        // Создание нового листа в рабочем файле с заданным названием
        Sheet sheet = workbook.createSheet(sheetLabel);

        // Определение стилей для ячеек
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss")); // Формат даты
        dateCellStyle.setAlignment(HorizontalAlignment.LEFT); // Выравнивание по левому краю

        CellStyle longCellStyle = workbook.createCellStyle();
        longCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0.00")); // Формат для чисел с двумя знаками после запятой
        longCellStyle.setAlignment(HorizontalAlignment.LEFT); // Выравнивание по левому краю

        // Установка ширины столбцов
        for (int i = 0; i < 5; i++) {
            sheet.setColumnWidth(i, 20 * 400); // Установка ширины для первых 5 столбцов
        }

        // Создание заголовков для информации о периоде
        Row infoHeader = sheet.createRow(0);
        infoHeader.createCell(0).setCellValue("Начиная с:"); // Заголовок "Начиная с:"
        Cell startDateCell = infoHeader.createCell(1);
        startDateCell.setCellValue(Date.from(from)); // Дата начала
        startDateCell.setCellStyle(dateCellStyle); // Применение стиля даты

        infoHeader.createCell(2).setCellValue("Заканчивая:"); // Заголовок "Заканчивая:"
        Cell endDateCell = infoHeader.createCell(3);
        endDateCell.setCellValue(Date.from(to)); // Дата окончания
        endDateCell.setCellStyle(dateCellStyle); // Применение стиля даты

        // Создание заголовков для таблицы с данными
        Row tableHeader = sheet.createRow(1);
        tableHeader.createCell(0).setCellValue("Время"); // Заголовок "Время"
        tableHeader.createCell(1).setCellValue("Открытие"); // Заголовок "Открытие"
        tableHeader.createCell(2).setCellValue("Закрытие"); // Заголовок "Закрытие"
        tableHeader.createCell(3).setCellValue("High"); // Заголовок "High"
        tableHeader.createCell(4).setCellValue("Low"); // Заголовок "Low"

        // Заполнение таблицы данными
        ArrayList<BigDecimal> openPrices = new ArrayList<>();
        ArrayList<BigDecimal> closePrices = new ArrayList<>();
        ArrayList<BigDecimal> highPrices = new ArrayList<>();
        ArrayList<BigDecimal> lowPrices = new ArrayList<>();
        int maxRows = historicCandleDtoList.size(); // Получение количества свечей
        for (int i = 0; i < maxRows; i++) {
            Row row = sheet.createRow(i + 2); // Создание новой строки для каждой свечи
            InstrumentHistoricCandleDto candle = historicCandleDtoList.get(i); // Получение данных свечи

            // Заполнение ячеек данными свечи
            Cell timeCell = row.createCell(0);
            timeCell.setCellValue(Date.from(candle.time())); // Время свечи
            timeCell.setCellStyle(dateCellStyle); // Применение стиля даты

            row.createCell(1).setCellValue(candle.openPrice().doubleValue()); // Цена открытия
            row.getCell(1).setCellStyle(longCellStyle); // Применение стиля для чисел
            openPrices.add(candle.openPrice());

            row.createCell(2).setCellValue(candle.closePrice().doubleValue()); // Цена закрытия
            row.getCell(2).setCellStyle(longCellStyle); // Применение стиля для чисел
            closePrices.add(candle.closePrice());

            row.createCell(3).setCellValue(candle.highPrice().doubleValue()); // Максимальная цена
            row.getCell(3).setCellStyle(longCellStyle); // Применение стиля для чисел
            highPrices.add(candle.highPrice());

            row.createCell(4).setCellValue(candle.lowPrice().doubleValue()); // Минимальная цена
            row.getCell(4).setCellStyle(longCellStyle); // Применение стиля для чисел
            lowPrices.add(candle.lowPrice());
        }

        Row resultRow = sheet.createRow(maxRows + 2); // Создание строки для результатов
        resultRow.createCell(0).setCellValue("Мат. ожидание");
        resultRow.createCell(1).setCellValue(mathService.getMathExpectation(openPrices).getResult());
        resultRow.createCell(2).setCellValue(mathService.getMathExpectation(closePrices).getResult());
        resultRow.createCell(3).setCellValue(mathService.getMathExpectation(highPrices).getResult());
        resultRow.createCell(4).setCellValue(mathService.getMathExpectation(lowPrices).getResult());

        // Запись данных в выходной поток
        workbook.write(outputStream);
        // Возвращение ресурса с массивом байтов, содержащим Excel файл
        return new ByteArrayResource(outputStream.toByteArray());
    }

}