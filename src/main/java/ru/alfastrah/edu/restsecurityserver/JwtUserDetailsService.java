package ru.alfastrah.edu.restsecurityserver;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    public static final String TOKEN_PREFIX = "Bearer ";
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration.seconds}")
    private Long expirationSeconds;

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
    }

    public String createJwtToken(UserDetails userDetails) {
        return Jwts.builder().setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000L))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public UserDetails loadUserByJwtToken(String jwtToken) {
        Claims claims = extractClaim(jwtToken);
        if (claims.getExpiration().before(new Date())) {
            throw new AccessDeniedException("Token has expired");
        }
        User user = loadUserByUsername(claims.getSubject());
        if (!user.getUsername().equals(claims.getSubject())) {
            throw new AccessDeniedException("Invalid username");
        }
        return user;
    }

    private Claims extractClaim(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
