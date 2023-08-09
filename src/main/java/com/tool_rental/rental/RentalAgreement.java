package com.tool_rental.rental;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tool_rental.rental.tools.Tool;
import com.tool_rental.utils.StringUtils;
import com.tool_rental.utils.serialization.MonetaryDeserializer;
import com.tool_rental.utils.serialization.MonetarySerializer;
import com.tool_rental.utils.serialization.PercentDeserializer;
import com.tool_rental.utils.serialization.PercentSerializer;

import java.time.LocalDate;

public class RentalAgreement {

    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final int rentalDays;
    private final LocalDate checkoutDate;
    private final LocalDate dueDate;
    @JsonSerialize(using = MonetarySerializer.class)
    @JsonDeserialize(using = MonetaryDeserializer.class)
    private final int dailyChargeCents;
    private final int chargeableDays;
    @JsonSerialize(using = MonetarySerializer.class)
    @JsonDeserialize(using = MonetaryDeserializer.class)
    private final int preDiscountChargeCents;
    @JsonSerialize(using = PercentSerializer.class)
    @JsonDeserialize(using = PercentDeserializer.class)
    private final int discountPercent;
    @JsonSerialize(using = MonetarySerializer.class)
    @JsonDeserialize(using = MonetaryDeserializer.class)
    private final int discountAmountCents;
    @JsonSerialize(using = MonetarySerializer.class)
    @JsonDeserialize(using = MonetaryDeserializer.class)
    private final int finalChargeCents;

    public RentalAgreement(Tool tool,
                           int rentalDays,
                           LocalDate checkoutDate,
                           LocalDate dueDate,
                           int chargeableDays,
                           int preDiscountChargeCents,
                           int discountPercent,
                           int discountAmountCents,
                           int finalChargeCents) {
        this.toolCode = tool.getCode();
        this.toolType = tool.getType().getTypeName();
        this.toolBrand = tool.getBrand();
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.dailyChargeCents = tool.getType().getDailyChargeCents();
        this.chargeableDays = chargeableDays;
        this.preDiscountChargeCents = preDiscountChargeCents;
        this.dueDate = dueDate;
        this.discountPercent = discountPercent;
        this.discountAmountCents = discountAmountCents;
        this.finalChargeCents = finalChargeCents;
    }

    @JsonCreator
    public RentalAgreement(String toolCode,
                           String toolType,
                           String toolBrand,
                           int rentalDays,
                           LocalDate checkoutDate,
                           LocalDate dueDate,
                           int dailyChargeCents,
                           int chargeableDays,
                           int preDiscountChargeCents,
                           int discountPercent,
                           int discountAmountCents,
                           int finalChargeCents) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.toolBrand = toolBrand;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dailyChargeCents = dailyChargeCents;
        this.chargeableDays = chargeableDays;
        this.preDiscountChargeCents = preDiscountChargeCents;
        this.discountPercent = discountPercent;
        this.discountAmountCents = discountAmountCents;
        this.finalChargeCents = finalChargeCents;
    }

    @Override
    public String toString() {
        return """
                tool code: %s
                tool type: %s
                tool brand: %s
                rental days: %s
                checkout date: %s
                due date: %s
                daily charge: %s
                chargeable days: %s
                pre-discount charge: %s
                discount percent: %s
                discount amount: %s
                final charge: %s
                """
                .formatted(
                        toolCode,
                        toolType,
                        toolBrand,
                        rentalDays,
                        StringUtils.formatDate(checkoutDate),
                        StringUtils.formatDate(dueDate),
                        StringUtils.formatUSD(dailyChargeCents),
                        chargeableDays,
                        StringUtils.formatUSD(preDiscountChargeCents),
                        StringUtils.formatPercent(discountPercent),
                        StringUtils.formatUSD(discountAmountCents),
                        StringUtils.formatUSD(finalChargeCents)
                );
    }
}
