package by.ares.authenticationservice.unit.service;

import by.ares.authenticationservice.dto.response.TokenDto;
import by.ares.authenticationservice.model.Account;
import by.ares.authenticationservice.model.Role;
import by.ares.authenticationservice.service.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static by.ares.authenticationservice.util.TestConstants.*;
import static by.ares.authenticationservice.util.TestModelBuilder.buildAccount;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    private Account account;
    private TokenDto tokenDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        account = buildAccount();
        tokenDto = jwtService.generateToken(account);
    }

    @Test
    void shouldGenerateAccessAndRefreshTokens() {
        assertNotNull(tokenDto);
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    void shouldValidateCorrectToken() {
        boolean valid = jwtService.validateToken(tokenDto.getAccessToken());
        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        boolean valid = jwtService.validateToken(INVALID_REFRESH_TOKEN);
        assertFalse(valid);
    }

    @Test
    void shouldExtractLoginFromToken() {
        String login = jwtService.extractLogin(tokenDto.getAccessToken());
        assertEquals(LOGIN, login);
    }

    @Test
    void shouldExtractUserIdFromToken() {
        Long userId = jwtService.extractUserId(tokenDto.getAccessToken());
        assertEquals(USER_ID, userId);
    }

    @Test
    void shouldExtractRoleFromToken() {
        Role role = jwtService.extractRole(tokenDto.getAccessToken());
        assertEquals(ROLE, role);
    }

    @Test
    void extractedValuesShouldMatchOriginalAccount() {
        String token = tokenDto.getAccessToken();
        assertAll(
                () -> assertEquals(account.getLogin(), jwtService.extractLogin(token)),
                () -> assertEquals(account.getUserId(), jwtService.extractUserId(token)),
                () -> assertEquals(account.getRole(), jwtService.extractRole(token))
        );
    }

}
