package com.tool_rental.utils;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.Monetary;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StringUtils {

    public static final MonetaryAmountFormat USD_STRING_FORMAT = MonetaryFormats.getAmountFormat(
            AmountFormatQueryBuilder.of(Locale.US)
                    .set(CurrencyStyle.SYMBOL)
                    .build()
    );

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yy");

    public static final String PERCENT_FORMAT = "%d%%";

    public static String formatUSD(int cents) {
        // convert cents to a MonetaryAmount
        var normalized = Money.ofMinor(
                Monetary.getCurrency("USD"),
                cents,
                2
        );

        return USD_STRING_FORMAT.format(normalized);
    }

    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMAT);
    }

    public static String formatPercent(int percent) {
        return PERCENT_FORMAT.formatted(percent);
    }

    public static int parseUsdToCents(String usdString) {
        return new BigDecimal(usdString.replace("$", ""))
                .movePointRight(2)
                .intValue();
    }

    public static int parsePercent(String percentString) {
        return Integer.parseInt(percentString.replace("%", ""));
    }
}
