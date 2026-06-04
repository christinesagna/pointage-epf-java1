package sn.epf.pointage.util;

import java.time.LocalDateTime;

public class DateUtils {
    public static boolean isBetween(LocalDateTime value, LocalDateTime start, LocalDateTime end) {
        return !value.isBefore(start) && !value.isAfter(end);
    }
}
