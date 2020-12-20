package ru.alfastrah.edu.restsecurityserver;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

import static ru.alfastrah.edu.restsecurityserver.JwtService.TOKEN_PREFIX;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final Function<String, UserDetails> parseJwtTokenFunction;

    public JwtAuthorizationFilter(AuthenticationManager authManager, Function<String, UserDetails> parseJwtTokenFunction) {
        super(authManager);
        this.parseJwtTokenFunction = parseJwtTokenFunction;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            try {
                UserDetails userDetails = parseJwtTokenFunction.apply(authHeader);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            } catch (RuntimeException e) {
                throw new BadCredentialsException("Failed to parse token: ");
            }
        }
        chain.doFilter(req, res);
    }
}
