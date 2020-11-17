package ru.alfastrah.edu.restsecurityserver.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/user")
    public void addUser(UserDto user) {
        customUserDetailsService.addUser(user.username, passwordEncoder.encode(user.password), user.fullname, user.department);
    }

    public static class UserDto {
        String username;
        String password;
        String fullname;
        String department;
    }
}
