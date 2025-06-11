package dev.remo.remo.Utils.General;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

    private DateUtil() {
    }

    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DATE_TIME_FORMATTER);
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }
}
