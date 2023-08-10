package com.tool_rental.utils;

import java.time.LocalDate;

public class DateUtils {

    public static boolean isWeekday(LocalDate date) {
        return !isWeekend(date);
    }

    public static boolean isWeekend(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case SATURDAY, SUNDAY -> true;
            default -> false;
        };
    }
}
