package ru.alfastrah.edu.restsecurityserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.temporal.ChronoUnit;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static ru.alfastrah.edu.restsecurityserver.ContractController.CONTRACT_BASE_URL;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ContractControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void getContractsTest() {
        String data = restTemplate.withBasicAuth("user", "password")
                .getForObject(CONTRACT_BASE_URL, String.class);
        assertEquals("[]", data);
    }

    @Test
    void getContractsWrongPasswordTest() {
        ResponseEntity<Contract> data = restTemplate.withBasicAuth("user", "wrong password")
                .getForEntity(CONTRACT_BASE_URL, Contract.class);
        assertEquals(HttpStatus.UNAUTHORIZED, data.getStatusCode());
    }

    @Test
    void postContractsAccessDeniedTest() {
        ResponseEntity<Contract> nonExistingContractEntity = restTemplate.withBasicAuth("user", "password")
                .postForEntity(CONTRACT_BASE_URL, generateContract(), Contract.class);
        assertEquals(HttpStatus.FORBIDDEN, nonExistingContractEntity.getStatusCode());
    }

    @Test
    void contractTest() {
        Contract contract = generateContract();

        Contract postContract = restTemplate.withBasicAuth("superuser", "password")
                .postForObject(CONTRACT_BASE_URL, contract, Contract.class);
        assertThat(postContract).isEqualToIgnoringGivenFields(contract, "id");
        assertNotNull(postContract.getId());

        Contract getContract = restTemplate.withBasicAuth("user", "password")
                .getForObject(CONTRACT_BASE_URL + "/" + postContract.getId(), Contract.class);
        assertEquals(postContract, getContract);

        restTemplate.withBasicAuth("superuser", "password")
                .delete(CONTRACT_BASE_URL + "/" + postContract.getId());

        ResponseEntity<Contract> nonExistingContractEntity = restTemplate.withBasicAuth("user", "password")
                .getForEntity(CONTRACT_BASE_URL + "/" + postContract.getId(), Contract.class);
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