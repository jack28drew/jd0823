package com.tool_rental.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    private static Stream<Arguments> provideMonetaryValuesForUSDFormatterCheck() {
        return Stream.of(
                Arguments.of(0, "$0.00"),
                Arguments.of(99, "$0.99"),
                Arguments.of(100, "$1.00"),
                Arguments.of(199, "$1.99")
        );
    }

    @ParameterizedTest
    @MethodSource("provideMonetaryValuesForUSDFormatterCheck")
    void testCentsToUsdString(int cents, String expectedUsdString) {
        assertThat(StringUtils.formatUSD(cents))
                .isEqualTo(expectedUsdString);
    }

    @Test
    void testDateFormatter() {
        var date = LocalDate.of(2023, 1, 2);

        assertThat(StringUtils.formatDate(date))
                .isEqualTo("01/02/23");
    }

    @Test
    void testPercentFormatter() {
        assertThat(StringUtils.formatPercent(5))
                .isEqualTo("5%");
    }

    private static Stream<Arguments> provideMonetaryStringsForUSDParserCheck() {
        return Stream.of(
                Arguments.of("$0.00", 0),
                Arguments.of("$0.99", 99),
                Arguments.of("$1.00", 100),
                Arguments.of("$1.99", 199)
        );
    }
    @ParameterizedTest
    @MethodSource("provideMonetaryStringsForUSDParserCheck")
    void testUsdStringToCentsParser(String usdString, int expectedCents) {
        assertThat(StringUtils.parseUsdToCents(usdString))
                .isEqualTo(expectedCents);
    }

    @Test
    void testParsePercent() {
        assertThat(StringUtils.parsePercent("5%"))
                .isEqualTo(5);
    }
}