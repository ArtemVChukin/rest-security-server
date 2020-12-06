package ru.alfastrah.edu.restsecurityserver;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ContractControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @RequiredArgsConstructor
    private static class AuthInterceptor implements ClientHttpRequestInterceptor {
        private final String authToken;

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {
            HttpHeaders headers = request.getHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, authToken);
            return execution.execute(request, body);
        }
    }

    @BeforeEach
    void signUp() {
        String authData = "{\"username\": \"superuser\",\"password\": \"password\"}";
        ResponseEntity<Void> authResponse = restTemplate.postForEntity("/login", authData, Void.class);
        String jwtToken = authResponse.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        restTemplate.getRestTemplate().setInterceptors(List.of(new AuthInterceptor(jwtToken)));
    }

    @Test
    void getContractsTest() {
        String data = restTemplate
                .getForObject("/contract", String.class);
        assertEquals("[]", data);
    }

    @Test
    void contractTest() {
        Contract contract = generateContract();

        Contract postContract = restTemplate
                .postForObject("/contract", contract, Contract.class);
        assertThat(postContract).isEqualToIgnoringGivenFields(contract, "id");
        assertNotNull(postContract.getId());

        Contract getContract = restTemplate
                .getForObject("/contract/" + postContract.getId(), Contract.class);
        assertEquals(postContract, getContract);

        restTemplate
                .delete("/contract/" + postContract.getId(), contract, Contract.class);

        ResponseEntity<Contract> nonExistingContractEntity = restTemplate
                .getForEntity("/contract/" + postContract.getId(), Contract.class);
        assertEquals(HttpStatus.NOT_FOUND, nonExistingContractEntity.getStatusCode());
    }

    private Contract generateContract() {
        Contract contract = new Contract();
        contract.setNumber("number");
        contract.setBeginDate(now());
        contract.setEndDate(now().plus(1, ChronoUnit.DAYS));
        contract.setDepartment("department");
        return contract;
    }
}