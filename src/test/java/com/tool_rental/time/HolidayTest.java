package com.tool_rental.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class HolidayTest {
    private static Stream<Arguments> provideDatesForIndependenceDayCheck() {
        return Stream.of(
                Arguments.of(LocalDate.of(2023, 7, 4), true), // Tues on Independence day
                Arguments.of(LocalDate.of(2026, 7, 4), false), // Sat on Independence day
                Arguments.of(LocalDate.of(2026, 7, 3), true), // Fri before Independence day (on Sat)
                Arguments.of(LocalDate.of(2027, 7, 5), true), // Mon after Independence day (on Sun)
                Arguments.of(LocalDate.of(2023, 8, 1), false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDatesForIndependenceDayCheck")
    void testIsIndependenceDayObserved(LocalDate date, boolean expectedResult) {
        assertThat(Holiday.INDEPENDENCE_DAY.isObserved(date))
                .isEqualTo(expectedResult);
    }

    private static Stream<Arguments> provideDatesForLaborDayCheck() {
        return Stream.of(
                Arguments.of(LocalDate.of(2023, 9, 4), true),
                Arguments.of(LocalDate.of(2023, 8, 31), false), // fails month check
                Arguments.of(LocalDate.of(2023, 9, 3), false),  // fails day-of-week check
                Arguments.of(LocalDate.of(2023, 9, 11), false)  // fails day-of-month check
        );
    }

    @ParameterizedTest
    @MethodSource("provideDatesForLaborDayCheck")
    void testIsLaborDayObserved(LocalDate date, boolean expectedResult) {
        assertThat(Holiday.LABOR_DAY.isObserved(date))
                .isEqualTo(expectedResult);
    }
    
    private static Stream<Arguments> provideDatesForHolidayCheck() {
        return Stream.of(
                Arguments.of(LocalDate.of(2023, 7, 4), true),
                Arguments.of(LocalDate.of(2023, 8, 4), false),
                Arguments.of(LocalDate.of(2023, 9, 4), true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDatesForHolidayCheck")
    void testAreHolidaysIdentifiedCorrectly(LocalDate date, boolean isHoliday) {
        assertThat(Holiday.isHoliday(date))
                .isEqualTo(isHoliday);
    }
}