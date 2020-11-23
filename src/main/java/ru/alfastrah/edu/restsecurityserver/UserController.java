package ru.alfastrah.edu.restsecurityserver;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/user")
    @SuppressWarnings("java:S4684")
    public User addUser(@RequestBody @Valid User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return customUserDetailsService.addUser(user);
    }

    @DeleteMapping("/user/{username}")
    public void addUser(@PathVariable String username) {
        customUserDetailsService.deleteByUsername(username);
    }

    @GetMapping("/user")
    public List<User> getUsers(@RequestParam(required = false) String username, @RequestParam(required = false) String department) {
        return customUserDetailsService.getUsers(username, department);
    }
}
