package com.tool_rental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractApiTest {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int serverPort;

    final String BASE_URL = "http://localhost:%d/api/tool-rental";

}
