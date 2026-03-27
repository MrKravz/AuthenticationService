package by.ares.authenticationservice.util;

import by.ares.authenticationservice.dto.request.AuthRequest;
import by.ares.authenticationservice.dto.request.RefreshTokenRequest;
import by.ares.authenticationservice.dto.request.RegisterRequest;
import by.ares.authenticationservice.dto.request.UserRequest;
import by.ares.authenticationservice.dto.response.TokenDto;
import by.ares.authenticationservice.model.Account;

import static by.ares.authenticationservice.util.TestConstants.*;

public class TestModelBuilder {

    public static Account buildAccount() {
        return new Account()
                .setLogin(LOGIN)
                .setPassword(RAW_PASSWORD)
                .setRole(ROLE)
                .setUserId(USER_ID);
    }

    public static Account buildSavedAccount() {
        return new Account()
                .setId(ACCOUNT_ID)
                .setUserId(USER_ID)
                .setPassword(ENCODED_PASSWORD);
    }

    public static RegisterRequest buildRegisterRequest() {
        return RegisterRequest
                .builder()
                .login(LOGIN)
                .password(RAW_PASSWORD)
                .userRequest(buildUserRequest())
                .build();
    }

    public static UserRequest buildUserRequest() {
        return UserRequest
                .builder()
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .birthDate(BIRTH_DATE)
                .build();
    }

    public static AuthRequest buildAuthRequest() {
        return AuthRequest
                .builder()
                .login(LOGIN)
                .password(ENCODED_PASSWORD)
                .build();
    }

    public static RefreshTokenRequest buildRefreshTokenRequest() {
        return new RefreshTokenRequest(VALID_REFRESH_TOKEN);
    }

    public static RefreshTokenRequest buildInvalidToken() {
        return new RefreshTokenRequest(INVALID_REFRESH_TOKEN);
    }

    public static TokenDto buildTokenDto() {
        return new TokenDto(ACCESS_TOKEN, VALID_REFRESH_TOKEN);
    }

}
