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

import static ru.alfastrah.edu.restsecurityserver.JwtUserDetailsService.TOKEN_PREFIX;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authManager, JwtUserDetailsService jwtUserDetailsService) {
        super(authManager);
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        setAuthentication(authHeader);
        chain.doFilter(req, res);
    }

    private void setAuthentication(String authHeader) {
        try {
            String jwtToken = authHeader.substring(TOKEN_PREFIX.length());
            UserDetails userDetails = jwtUserDetailsService.loadUserByJwtToken(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        } catch (RuntimeException e) {
            throw new BadCredentialsException("Failed to parse token: ");
        }
    }
}
