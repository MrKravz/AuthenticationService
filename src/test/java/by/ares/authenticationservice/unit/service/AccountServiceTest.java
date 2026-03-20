package by.ares.authenticationservice.unit.service;

import by.ares.authenticationservice.dto.request.AuthRequest;
import by.ares.authenticationservice.dto.request.RefreshTokenRequest;
import by.ares.authenticationservice.dto.request.RegisterRequest;
import by.ares.authenticationservice.dto.response.TokenDto;
import by.ares.authenticationservice.exception.AccountNotFoundException;
import by.ares.authenticationservice.exception.InvalidRefreshTokenException;
import by.ares.authenticationservice.exception.LoginAlreadyExistsException;
import by.ares.authenticationservice.model.Account;
import by.ares.authenticationservice.repository.AccountRepository;
import by.ares.authenticationservice.service.impl.AccountServiceImpl;
import by.ares.authenticationservice.service.ApiClientService;
import by.ares.authenticationservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static by.ares.authenticationservice.util.TestConstants.*;
import static by.ares.authenticationservice.util.TestModelBuilder.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private ApiClientService apiClientService;

    @InjectMocks
    private AccountServiceImpl accountService;


    private RegisterRequest registerRequest;
    private AuthRequest authRequest;
    private RefreshTokenRequest invalidRefreshTokenRequest;
    private Account accountAfterSave;

    private RefreshTokenRequest refreshTokenRequest;
    private TokenDto token;

    @BeforeEach
    void init() {
        registerRequest = buildRegisterRequest();
        accountAfterSave = buildSavedAccount();
        authRequest = buildAuthRequest();
        refreshTokenRequest = buildRefreshTokenRequest();
        invalidRefreshTokenRequest = buildInvalidToken();
        token = buildTokenDto();
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(accountRepository.existsAccountByLogin(LOGIN)).thenReturn(false);
        when(apiClientService.createUser(registerRequest.getUserRequest())).thenReturn(USER_ID);
        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(Account.class)))
                .thenReturn(token);
        TokenDto result = accountService.register(registerRequest);
        assertEquals(token, result);
        verify(apiClientService).createUser(registerRequest.getUserRequest());
        verify(accountRepository).save(any(Account.class));
        verify(jwtService).generateToken(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenLoginAlreadyExists() {
        when(accountRepository.existsAccountByLogin(LOGIN)).thenReturn(true);
        assertThrows(LoginAlreadyExistsException.class,
                () -> accountService.register(registerRequest));
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        when(accountRepository.findByLogin(LOGIN)).thenReturn(Optional.of(accountAfterSave));
        when(jwtService.generateToken(accountAfterSave)).thenReturn(token);
        TokenDto result = accountService.authenticate(authRequest);
        assertEquals(token, result);
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundDuringAuth() {
        when(accountRepository.findByLogin(LOGIN)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class,
                () -> accountService.authenticate(authRequest));
    }

    @Test
    void shouldRefreshTokenSuccessfully() {
        when(jwtService.validateRefreshToken(VALID_REFRESH_TOKEN)).thenReturn(true);
        when(jwtService.extractLogin(VALID_REFRESH_TOKEN)).thenReturn(LOGIN);
        when(accountRepository.findByLogin(LOGIN)).thenReturn(Optional.of(accountAfterSave));
        when(jwtService.generateToken(accountAfterSave)).thenReturn(token);
        TokenDto result = accountService.refreshToken(refreshTokenRequest);
        assertEquals(token, result);
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenInvalid() {
        when(jwtService.validateRefreshToken(INVALID_REFRESH_TOKEN)).thenReturn(false);
        assertThrows(InvalidRefreshTokenException.class,
                () -> accountService.refreshToken(invalidRefreshTokenRequest));
    }

}