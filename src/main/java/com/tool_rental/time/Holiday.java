package com.tool_rental.time;

import com.tool_rental.utils.DateUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Arrays;

public enum Holiday {

    INDEPENDENCE_DAY(7, 4),
    LABOR_DAY {
        @Override
        public boolean isObserved(LocalDate date) {
            return date.getMonth() == Month.SEPTEMBER
                    && date.getDayOfWeek() == DayOfWeek.MONDAY
                    && date.getDayOfMonth() <= 7;
        }
    };

    private final MonthDay HOLIDAY_DATE;
    private final boolean IS_FIXED_DATE;

    /**
     * Constructor for holidays with fixed dates, providing both the month and day of the holiday
     * @param month
     * @param day
     */
    Holiday(int month, int day) {
        this.HOLIDAY_DATE = MonthDay.of(month, day);
        this.IS_FIXED_DATE = true;
    }

    /**
     * Constructor for holidays that do not have a fixed date, but are determined by a set of rules instead.
     * Holidays without a fixed date will need to override the isObserved() method to define those rules.
     */
    Holiday() {
        this.HOLIDAY_DATE = null;
        this.IS_FIXED_DATE = false;
    }

    /**
     * Returns true if the provided date is the observation date of any holidays enumerated here.
     * @param date
     * @return
     */
    public static boolean isHoliday(LocalDate date) {
        return Arrays.stream(Holiday.values())
                .anyMatch(holiday -> holiday.isObserved(date));
    }

    public boolean isObserved(LocalDate date) {
        if (!IS_FIXED_DATE) {
            throw new RuntimeException("Holidays without fixed dates must override the implementation of isObserved()");
        }

        return isWeekdayHoliday(date, HOLIDAY_DATE)
                || isFridayOfObservation(date, HOLIDAY_DATE)
                || isMondayOfObservation(date, HOLIDAY_DATE);
    }

    private static boolean isWeekdayHoliday(LocalDate date, MonthDay holidayDate) {
        return MonthDay.from(date).equals(holidayDate)
                && DateUtils.isWeekday(date);
    }

    private static boolean isFridayOfObservation(LocalDate date, MonthDay holidayDate) {
        return date.getDayOfWeek() == DayOfWeek.FRIDAY
                && MonthDay.from(date.plusDays(1)).equals(holidayDate);
    }

    private static boolean isMondayOfObservation(LocalDate date, MonthDay holidayDate) {
        return date.getDayOfWeek() == DayOfWeek.MONDAY
                && MonthDay.from(date.minusDays(1)).equals(holidayDate);
    }
}
