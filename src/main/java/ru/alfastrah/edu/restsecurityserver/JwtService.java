package ru.alfastrah.edu.restsecurityserver;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    public static final String TOKEN_PREFIX = "Bearer ";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration.seconds}")
    private Long expirationSeconds;

    public UserDetails parseJwtToken(String token) {
        String jwtToken = token.substring(TOKEN_PREFIX.length());
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken).getBody();
        if (claims.getExpiration().before(new Date())) {
            throw new AccessDeniedException("Token has expired");
        }
        User user = new User();
        user.setUsername(claims.getSubject());
        user.setRoles(claims.get("Authority", String.class));
        return user;
    }

    public String createJwtToken(UserDetails userDetails) {
        String authority = !userDetails.getAuthorities().isEmpty()
                ? userDetails.getAuthorities().iterator().next().getAuthority()
                : "";
        return TOKEN_PREFIX + Jwts.builder().setClaims(new HashMap<>(Map.of("Authority", authority)))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000L))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}
