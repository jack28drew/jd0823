package com.tool_rental.rental;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class CheckoutRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validCheckoutRequestPassesValidation() {
        var request = new RentalController.CheckoutRequest(
                "ABC",
                LocalDate.now(),
                1,
                5
        );

        assertThat(validator.validate(request))
                .isEmpty();
    }

    @Test
    void checkoutRequestWithBlankToolCodeFailsValidation() {
        var request = new RentalController.CheckoutRequest(
                "",
                LocalDate.now(),
                1,
                5
        );

        assertValidationViolations(validator.validate(request),
                "toolCode", "must not be blank"
        );
    }

    @Test
    void checkoutRequestWithNullToolCodeFailsValidation() {
        var request = new RentalController.CheckoutRequest(
                null,
                LocalDate.now(),
                1,
                5
        );

        assertValidationViolations(validator.validate(request),
                "toolCode", "must not be blank"
        );
    }

    @Test
    void checkoutRequestWithNullCheckoutDateFailsValidation() {
        var request = new RentalController.CheckoutRequest(
                "ABC",
                null,
                1,
                5
        );

        assertValidationViolations(validator.validate(request),
                "checkoutDate", "must not be null"
        );
    }

    @Test
    void checkoutRequestWithRentalDaysLessThanOneFailsValidation() {
        var request = new RentalController.CheckoutRequest(
                "ABC",
                LocalDate.now(),
                0,
                5
        );

        assertValidationViolations(validator.validate(request),
                "rentalDays", "Tools cannot be rented for less than 1 day"
        );
    }

    @Test
    void checkoutRequestWithDiscountPercentLessThanOneFailsValidation() {
        var request = new RentalController.CheckoutRequest(
                "ABC",
                LocalDate.now(),
                5,
                -1
        );

        assertValidationViolations(validator.validate(request),
                "discountPercent", "A negative discount cannot be applied!"
        );
    }

    @Test
    void checkoutRequestWithDiscountPercentGreaterThanOneHundredFailsValidation() {
        var request = new RentalController.CheckoutRequest(
                "ABC",
                LocalDate.now(),
                5,
                101
        );

        assertValidationViolations(validator.validate(request),
                "discountPercent", "A discount greater than 100 percent cannot be applied!"
        );
    }

    private void assertValidationViolations(Set<ConstraintViolation<RentalController.CheckoutRequest>> violations,
                                            String invalidField,
                                            String errorMsg) {
        assertThat(violations)
                .extracting(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        tuple(PathImpl.createPathFromString(invalidField), errorMsg)
                );
    }
}