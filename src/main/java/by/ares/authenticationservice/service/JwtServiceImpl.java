package by.ares.authenticationservice.service;

import by.ares.authenticationservice.dto.response.TokenDto;
import by.ares.authenticationservice.model.Account;
import by.ares.authenticationservice.model.Role;
import by.ares.authenticationservice.service.abstraction.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static by.ares.authenticationservice.util.AuthServiceConstants.*;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret.key}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public TokenDto generateToken(Account account) {
        String accessToken = generateAccessToken(account);
        String refreshToken = generateRefreshToken(account);
        return new TokenDto(accessToken, refreshToken);
    }

    private String generateAccessToken(Account account) {
        return Jwts.builder()
                .setSubject(account.getLogin())
                .claim(claimNameUserId, account.getUserId())
                .claim(claimNameRole, account.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    private String generateRefreshToken(Account account) {
        return Jwts.builder()
                .setSubject(account.getLogin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String extractLogin(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public Long extractUserId(String token) {
        return extractClaims(token).get(claimNameUserId, Long.class);
    }

    @Override
    public Role extractRole(String token) {
        return Role.valueOf(extractClaims(token).get(claimNameRole, String.class));
    }

}
