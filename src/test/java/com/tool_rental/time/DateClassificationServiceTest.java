package com.tool_rental.time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class DateClassificationServiceTest {

    private DateClassificationService testClass;

    @BeforeEach
    void setup() {
        testClass = new DateClassificationService();
    }

    @Test
    void testClassifyDatesInRange_withAllDateClassifications() {
        var startDate = LocalDate.parse("2021-07-02");
        var endDate = LocalDate.parse("2021-07-05");

        var classifiedDates = testClass.classifyDatesInRange(startDate, endDate);

        assertThat(classifiedDates.holidays())
                .containsExactlyInAnyOrder(LocalDate.parse("2021-07-05"));

        assertThat(classifiedDates.weekdays())
                .containsExactlyInAnyOrder(LocalDate.parse("2021-07-02"));

        assertThat(classifiedDates.weekends())
                .containsExactlyInAnyOrder(
                        LocalDate.parse("2021-07-03"),
                        LocalDate.parse("2021-07-04")
                );
    }

    @Test
    void testClassifyDatesInRange_withEmptyDateClassifications() {
        var startDate = LocalDate.parse("2023-08-01");
        var endDate = LocalDate.parse("2023-08-01");

        var classifiedDates = testClass.classifyDatesInRange(startDate, endDate);

        assertThat(classifiedDates.weekdays())
                .containsExactlyInAnyOrder(LocalDate.parse("2023-08-01"));

        assertThat(classifiedDates.holidays())
                .isEmpty();

        assertThat(classifiedDates.weekends())
                .isEmpty();
    }

    @Test
    void testClassifyDatesInRange_WithInvertedDates_throwsException() {
        var startDate = LocalDate.parse("2021-07-05");
        var endDate = LocalDate.parse("2021-07-02");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> testClass.classifyDatesInRange(startDate, endDate))
                .withMessage("2021-07-03 < 2021-07-05");
    }
}