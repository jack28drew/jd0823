package com.tool_rental.rental;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
class RentalController {

    private final CheckoutService checkoutService;

    @Autowired
    RentalController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping(value = "/checkout", consumes = "application/json", produces = "application/json")
    RentalAgreement checkout(@Valid @RequestBody CheckoutRequest request) {
        return checkoutService.checkout(
                request.toolCode,
                request.checkoutDate,
                request.rentalDays,
                request.discountPercent
        );

    }

    record CheckoutRequest(@NotBlank String toolCode,
                           @NotNull LocalDate checkoutDate,
                           @Min(value = 1, message = "Tools cannot be rented for less than 1 day") Integer rentalDays,
                           @Min(value = 0, message = "A negative discount cannot be applied!")
                           @Max(value = 100, message = "A discount greater than 100 percent cannot be applied!") Integer discountPercent) {
        @JsonCreator
        CheckoutRequest(String toolCode, LocalDate checkoutDate, Integer rentalDays, Integer discountPercent) {
            this.toolCode = toolCode;
            this.checkoutDate = checkoutDate;
            this.rentalDays = rentalDays;
            this.discountPercent = discountPercent;
        }
    }
}
