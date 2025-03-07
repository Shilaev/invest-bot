package ru.shilaev.investvisor.service;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import ru.shilaev.investvisor.dto.model.InstrumentHistoricCandleDto;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.Date;
import java.util.ArrayList;

@Service
public class ExcelService {

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
        dateCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss")); // Формат даты
        dateCellStyle.setAlignment(HorizontalAlignment.LEFT); // Выравнивание по левому краю

        CellStyle longCellStyle = workbook.createCellStyle();
        longCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0.00")); // Формат для чисел с двумя знаками после запятой
        longCellStyle.setAlignment(HorizontalAlignment.LEFT); // Выравнивание по левому краю

        // Установка ширины столбцов
        sheet.setColumnWidth(0, 20 * 400); // A
        sheet.setColumnWidth(1, 20 * 400); // B
        sheet.setColumnWidth(2, 20 * 400); // C
        sheet.setColumnWidth(3, 20 * 400); // D
        sheet.setColumnWidth(4, 20 * 400); // E

        // Создание заголовков для информации о периоде
        Row infoHeader = sheet.createRow(0);
        infoHeader.createCell(0).setCellValue("Начиная с:"); // Заголовок "Начиная с:"
        infoHeader.createCell(1).setCellValue(Date.from(from)); // Дата начала
        infoHeader.getCell(1).setCellStyle(dateCellStyle); // Применение стиля даты

        infoHeader.createCell(2).setCellValue("Заканчивая:"); // Заголовок "Заканчивая:"
        infoHeader.createCell(3).setCellValue(Date.from(to)); // Дата окончания
        infoHeader.getCell(3).setCellStyle(dateCellStyle); // Применение стиля даты

        // Создание заголовков для таблицы с данными
        Row tableHeader = sheet.createRow(1);
        tableHeader.createCell(0).setCellValue("Время"); // Заголовок "Время"
        tableHeader.createCell(1).setCellValue("Открытие"); // Заголовок "Открытие"
        tableHeader.createCell(2).setCellValue("Закрытие"); // Заголовок "Закрытие"
        tableHeader.createCell(3).setCellValue("High"); // Заголовок "High"
        tableHeader.createCell(4).setCellValue("Low"); // Заголовок "Low"

        // Заполнение таблицы данными
        int maxRows = historicCandleDtoList.size(); // Получение количества свечей
        for (int i = 0; i < maxRows; i++) {
            Row row = sheet.createRow(i + 2); // Создание новой строки для каждой свечи
            InstrumentHistoricCandleDto candle = historicCandleDtoList.get(i); // Получение данных свечи

            // Заполнение ячеек данными свечи
            row.createCell(0).setCellValue(Date.from(candle.time())); // Время свечи
            row.getCell(0).setCellStyle(dateCellStyle); // Применение стиля даты

            row.createCell(1).setCellValue(candle.openPrice().doubleValue()); // Цена открытия
            row.getCell(1).setCellStyle(longCellStyle); // Применение стиля для чисел

            row.createCell(2).setCellValue(candle.closePrice().doubleValue()); // Цена закрытия
            row.getCell(2).setCellStyle(longCellStyle); // Применение стиля для чисел

            row.createCell(3).setCellValue(candle.highPrice().doubleValue()); // Максимальная цена
            row.getCell(3).setCellStyle(longCellStyle); // Применение стиля для чисел

            row.createCell(4).setCellValue(candle.lowPrice().doubleValue()); // Минимальная цена
            row.getCell(4).setCellStyle(longCellStyle); // Применение стиля для чисел
        }

        // Запись данных в выходной поток
        workbook.write(outputStream);
        // Возвращение ресурса с массивом байтов, содержащим Excel файл
        return new ByteArrayResource(outputStream.toByteArray());
    }

}