package ru.alfastrah.edu.restsecurityserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.ResourceAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserControllerTest {
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @SuppressWarnings("java:S5778")
    void postUserWrongPasswordTest() {
        assertThrows(ResourceAccessException.class, () -> restTemplate.withBasicAuth("admin", "wrong password")
                .postForObject("/user", generateUser(), User.class));
    }

    @Test
    void postUserAccessDeniedTest() {
        ResponseEntity<User> blank = restTemplate.withBasicAuth("user", "password")
                .postForEntity("/user", generateUser(), User.class);
        assertEquals(HttpStatus.FORBIDDEN, blank.getStatusCode());
    }

    @Test
    void postUser() {
        User user = generateUser();
        User postUser = restTemplate.withBasicAuth("admin", "password")
                .postForObject("/user", user, User.class);
        assertThat(postUser).isEqualToIgnoringGivenFields(user, "id", "password");
        assertNotNull(postUser.getId());
        assertNotNull(postUser.getPassword());
    }

    private User generateUser() {
        User user = new User();
        user.setUsername("test_user");
        user.setFullname("new user");
        user.setDepartment("main");
        user.setPassword("password");
        user.setRoles("ROLE_USER");
        return user;
    }
}