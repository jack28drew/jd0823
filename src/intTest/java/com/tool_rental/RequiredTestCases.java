package com.tool_rental;

import com.tool_rental.rental.RentalAgreement;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RequiredTestCases extends AbstractApiTest {

    private String url;

    @BeforeEach
    void setup() {
        url = BASE_URL.formatted(serverPort) + "/checkout";
    }

    @Test
    void test1() {
        var requestBody = buildRequestBody(
                "JAKR",
                "09/03/15",
                5,
                101
        );

        var response = checkout(requestBody, String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .contains("A discount greater than 100 percent cannot be applied!");
    }

    @Test
    void test2() {
        var requestBody = buildRequestBody(
                "LADW",
                "07/02/20",
                3,
                10
        );

        var response = checkout(requestBody, RentalAgreement.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString())
                .isEqualToIgnoringWhitespace("""
                        tool code: LADW
                        tool type: LADDER
                        tool brand: WERNER
                        rental days: 3
                        checkout date: 07/02/20
                        due date: 07/05/20
                        daily charge: $1.99
                        chargeable days: 2
                        pre-discount charge: $3.98
                        discount percent: 10%
                        discount amount: $0.40
                        final charge: $3.58
                        """);
    }

    @Test
    void test3() {
        var requestBody = buildRequestBody(
                "CHNS",
                "07/02/15",
                5,
                25
        );

        var response = checkout(requestBody, RentalAgreement.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString())
                .isEqualToIgnoringWhitespace("""
                        tool code: CHNS
                        tool type: CHAINSAW
                        tool brand: STIHL
                        rental days: 5
                        checkout date: 07/02/15
                        due date: 07/07/15
                        daily charge: $1.49
                        chargeable days: 3
                        pre-discount charge: $4.47
                        discount percent: 25%
                        discount amount: $1.12
                        final charge: $3.35
                        """);
    }

    @Test
    void test4() {
        var requestBody = buildRequestBody(
                "JAKD",
                "09/03/15",
                6,
                0
        );

        var response = checkout(requestBody, RentalAgreement.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString())
                .isEqualToIgnoringWhitespace("""
                        tool code: JAKD
                        tool type: JACKHAMMER
                        tool brand: DEWALT
                        rental days: 6
                        checkout date: 09/03/15
                        due date: 09/09/15
                        daily charge: $2.99
                        chargeable days: 3
                        pre-discount charge: $8.97
                        discount percent: 0%
                        discount amount: $0.00
                        final charge: $8.97
                        """);
    }

    @Test
    void test5() {
        var requestBody = buildRequestBody(
                "JAKR",
                "07/02/15",
                9,
                0
        );

        var response = checkout(requestBody, RentalAgreement.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString())
                .isEqualToIgnoringWhitespace("""
                        tool code: JAKR
                        tool type: JACKHAMMER
                        tool brand: RIDGID
                        rental days: 9
                        checkout date: 07/02/15
                        due date: 07/11/15
                        daily charge: $2.99
                        chargeable days: 5
                        pre-discount charge: $14.95
                        discount percent: 0%
                        discount amount: $0.00
                        final charge: $14.95
                        """);
    }

    @Test
    void test6() {
        var requestBody = buildRequestBody(
                "JAKR",
                "07/02/20",
                4,
                50
        );

        var response = checkout(requestBody, RentalAgreement.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString())
                .isEqualToIgnoringWhitespace("""
                        tool code: JAKR
                        tool type: JACKHAMMER
                        tool brand: RIDGID
                        rental days: 4
                        checkout date: 07/02/20
                        due date: 07/06/20
                        daily charge: $2.99
                        chargeable days: 1
                        pre-discount charge: $2.99
                        discount percent: 50%
                        discount amount: $1.50
                        final charge: $1.49
                        """);
    }

    private static String buildRequestBody(String toolCode,
                                           String checkoutDate,
                                           int rentalDays,
                                           int discountPercent) {
        String request;
        try {
            request = new JSONObject()
                    .put("tool_code", toolCode)
                    .put("checkout_date", checkoutDate)
                    .put("rental_days", rentalDays)
                    .put("discount_percent", discountPercent)
                    .toString();
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
        return request;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private <T> ResponseEntity<T> checkout(String request, Class<T> responseType) {
        return restTemplate.postForEntity(
                url,
                new HttpEntity<>(request, buildHeaders()),
                responseType
        );
    }
}
