package com.tool_rental.rental;

import com.tool_rental.rental.tools.Tool;
import com.tool_rental.rental.tools.ToolType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class RentalAgreementTest {

    @Test
    void testToString() {
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

        var rentalAgreement = new RentalAgreement(
                wernerLadder,
                1,
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 2),
                1,
                199,
                0,
                0,
                199
        );

        assertThat(rentalAgreement.toString())
                .isEqualTo(
                        """
                                tool code: LADW
                                tool type: Ladder
                                tool brand: Werner
                                rental days: 1
                                checkout date: 01/01/20
                                due date: 01/02/20
                                daily charge: $1.99
                                chargeable days: 1
                                pre-discount charge: $1.99
                                discount percent: 0%
                                discount amount: $0.00
                                final charge: $1.99
                                        """
                );
    }
}