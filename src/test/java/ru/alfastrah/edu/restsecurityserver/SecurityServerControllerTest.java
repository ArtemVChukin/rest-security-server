package ru.alfastrah.edu.restsecurityserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SecurityServerControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void getContractTest() {
        String data = restTemplate.withBasicAuth("admin", "password")
                .getForObject("/contract", String.class);
        assertEquals("[]", data);
    }
}