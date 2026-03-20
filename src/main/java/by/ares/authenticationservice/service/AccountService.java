package by.ares.authenticationservice.service;

import by.ares.authenticationservice.dto.request.AccessTokenRequest;
import by.ares.authenticationservice.dto.request.AuthRequest;
import by.ares.authenticationservice.dto.request.RefreshTokenRequest;
import by.ares.authenticationservice.dto.request.RegisterRequest;
import by.ares.authenticationservice.dto.response.TokenDto;

public interface AccountService {
    TokenDto register(RegisterRequest request);
    TokenDto authenticate(AuthRequest request);
    TokenDto refreshToken(RefreshTokenRequest request);
    Boolean validate(AccessTokenRequest accessTokenRequest);
}
