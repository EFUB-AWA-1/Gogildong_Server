package com.efub.gogildong.global.util;

import java.io.PrintWriter;
import java.util.List;

public class CsvUtils {

    public static <T> void writeToCsv(PrintWriter writer, List<T> data, String[] header, CsvMapper<T> mapper) {
        try (com.opencsv.CSVWriter csvWriter = new com.opencsv.CSVWriter(writer)) {
            csvWriter.writeNext(header);
            for (T item : data) {
                csvWriter.writeNext(mapper.map(item));
            }
        } catch (Exception e) {
            throw new RuntimeException("CSV 변환 실패", e);
        }
    }

    public interface CsvMapper<T> {
        String[] map(T dto);
    }
}
