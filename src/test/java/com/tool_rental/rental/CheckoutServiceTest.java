package com.tool_rental.rental;

import com.tool_rental.rental.tools.Tool;
import com.tool_rental.rental.tools.ToolRepository;
import com.tool_rental.rental.tools.ToolType;
import com.tool_rental.time.DateClassificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private ToolRepository toolRepository;
    @Mock
    private DateClassificationService dateClassificationService;

    private CheckoutService checkoutService;

    @BeforeEach
    void setup() {
        checkoutService = new CheckoutService(toolRepository, dateClassificationService);
    }

    @Test
    void checkout_withNoMatchingTool_throwsException() {
        var toolCode = "ABC";

        when(toolRepository.findByCode(toolCode))
                .thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> checkoutService.checkout(toolCode, LocalDate.now(), 1, 0))
                .withMessage("No tool found for code [%s]".formatted(toolCode));
    }

    @Test
    void checkout_withValidArguments_returnsRentalAgreement() {
        var wernerLadder = new Tool(
                "LADW",
                new ToolType(
                        "Ladder",
                        199,
                        true,
                        true,
                        false
                ),
                "Werner"
        );

        var rentalDate = LocalDate.parse("2020-07-02");

        when(toolRepository.findByCode(wernerLadder.getCode()))
                .thenReturn(Optional.of(wernerLadder));

        when(dateClassificationService.classifyDatesInRange(LocalDate.parse("2020-07-03"), LocalDate.parse("2020-07-05")))
                .thenReturn(new DateClassificationService.ClassifiedDates(
                        Set.of(LocalDate.parse("2020-07-03")),
                        Set.of(LocalDate.parse("2020-07-04"), LocalDate.parse("2020-07-05")),
                        Set.of()
                ));

        var checkoutResult = checkoutService.checkout(wernerLadder.getCode(), rentalDate, 3, 10);

        assertThat(checkoutResult)
                .usingRecursiveComparison()
                .isEqualTo(new RentalAgreement(
                        wernerLadder,
                        3,
                        rentalDate,
                        LocalDate.parse("2020-07-05"),
                        2,
                        398,
                        10,
                        40,
                        358
                ));
    }
}