package by.ares.authenticationservice.service;

import by.ares.authenticationservice.dto.response.TokenDto;
import by.ares.authenticationservice.model.Account;
import by.ares.authenticationservice.model.Role;

public interface JwtService {
    TokenDto generateToken(Account account);

    boolean validateRefreshToken(String token);

    boolean validateAccessToken(String token);

    String extractLogin(String token);

    Long extractUserId(String token);

    Role extractRole(String token);

}
