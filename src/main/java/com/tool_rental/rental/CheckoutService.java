package com.tool_rental.rental;

import com.tool_rental.rental.tools.Tool;
import com.tool_rental.rental.tools.ToolRepository;
import com.tool_rental.time.DateClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class CheckoutService {

    private final ToolRepository toolRepository;
    private final DateClassificationService dateClassificationService;

    @Autowired
    public CheckoutService(ToolRepository toolRepository, DateClassificationService dateClassificationService) {
        this.toolRepository = toolRepository;
        this.dateClassificationService = dateClassificationService;
    }

    public RentalAgreement checkout(String toolCode,
                                    LocalDate checkoutDate,
                                    int rentalDays,
                                    int discountPercent) {

        var tool = toolRepository.findByCode(toolCode)
                .orElseThrow(() -> new IllegalArgumentException("No tool found for code [%s]".formatted(toolCode)));

        var dueDate = calculateDueDate(checkoutDate, rentalDays);
        var chargeableDays = calculateChargeableDays(tool, checkoutDate, dueDate);
        var preDiscountChargeCents = calculatePreDiscountCharge(chargeableDays, tool);
        var discountAmountCents = calculateDiscount(preDiscountChargeCents, discountPercent);
        var finalChargeCents = calculateFinalCharge(preDiscountChargeCents, discountAmountCents);

        return new RentalAgreement(
                tool,
                rentalDays,
                checkoutDate,
                dueDate,
                chargeableDays,
                preDiscountChargeCents,
                discountPercent,
                discountAmountCents,
                finalChargeCents
        );
    }

    private LocalDate calculateDueDate(LocalDate checkoutDate, int rentalDays) {
        return checkoutDate.plusDays(rentalDays);
    }

    private int calculateChargeableDays(Tool tool, LocalDate checkoutDate, LocalDate dueDate) {
        var classifiedDates = dateClassificationService.classifyDatesInRange(checkoutDate.plusDays(1), dueDate);
        var toolType = tool.getType();

        int chargeableDays = 0;
        if (toolType.hasHolidayCharge()) {
            chargeableDays += classifiedDates.holidays().size();
        }
        if (toolType.hasWeekdayCharge()) {
            chargeableDays += classifiedDates.weekdays().size();
        }
        if (toolType.hasWeekendCharge()) {
            chargeableDays += classifiedDates.weekends().size();
        }
        return chargeableDays;
    }

    private int calculatePreDiscountCharge(int chargeableDays, Tool tool) {
        return tool.getType().getDailyChargeCents() * chargeableDays;
    }

    private int calculateDiscount(int preDiscountChargeCents, int discountPercent) {
        return new BigDecimal(preDiscountChargeCents)
                .multiply(new BigDecimal(discountPercent).movePointLeft(2))  // shift decimal point left to convert to decimal
                .setScale(0, RoundingMode.HALF_UP)  // round away any fractional cents
                .intValue();
    }

    private int calculateFinalCharge(int preDiscountChargeCents, int discountAmountCents) {
        return preDiscountChargeCents - discountAmountCents;
    }
}
