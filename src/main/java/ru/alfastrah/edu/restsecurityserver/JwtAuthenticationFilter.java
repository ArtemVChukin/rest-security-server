package ru.alfastrah.edu.restsecurityserver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final Function<UserDetails, String> createJwtTokenFunction;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, Function<UserDetails, String> createJwtTokenFunction) {
        this.authenticationManager = authenticationManager;
        this.createJwtTokenFunction = createJwtTokenFunction;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        try {
            AuthRequest authRequest = objectMapper.readValue(req.getInputStream(), AuthRequest.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {
        String token = createJwtTokenFunction.apply((UserDetails) auth.getPrincipal());
        res.addHeader(HttpHeaders.AUTHORIZATION, token);
    }

    @Getter
    static class AuthRequest {
        String username;
        String password;
    }
}
