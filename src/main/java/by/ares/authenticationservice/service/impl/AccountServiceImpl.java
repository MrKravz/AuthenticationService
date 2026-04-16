package by.ares.authenticationservice.service.impl;

import by.ares.authenticationservice.dto.request.AccessTokenRequest;
import by.ares.authenticationservice.dto.request.AuthRequest;
import by.ares.authenticationservice.dto.request.RefreshTokenRequest;
import by.ares.authenticationservice.dto.request.RegisterRequest;
import by.ares.authenticationservice.dto.response.TokenDto;
import by.ares.authenticationservice.exception.AccountNotFoundException;
import by.ares.authenticationservice.exception.InvalidRefreshTokenException;
import by.ares.authenticationservice.exception.LoginAlreadyExistsException;
import by.ares.authenticationservice.model.Account;
import by.ares.authenticationservice.repository.AccountRepository;
import by.ares.authenticationservice.service.AccountService;
import by.ares.authenticationservice.service.ApiClientService;
import by.ares.authenticationservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static by.ares.authenticationservice.util.AuthServiceConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ApiClientService apiClientService;


    @Override
    @Transactional
    public TokenDto register(RegisterRequest request) {
        if (accountRepository.existsAccountByLogin(request.getLogin())) {
            throw new LoginAlreadyExistsException(LOGIN_ALREADY_EXISTS_MESSAGE);
        }
        Long userId = apiClientService.createUser(request.getUserRequest());
        synchronizeRollback(userId);
        Account account = new Account()
                .setLogin(request.getLogin())
                .setUserId(userId)
                .setPassword(passwordEncoder.encode(request.getPassword()));
        accountRepository.save(account);
        return jwtService.generateToken(account);
    }

    @Override
    public TokenDto authenticate(AuthRequest request) {
        Account account = accountRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(),
                request.getPassword()));
        return jwtService.generateToken(account);
    }

    @Override
    public TokenDto refreshToken(RefreshTokenRequest request) {
        if (!jwtService.validateRefreshToken(request.getRefreshToken())) {
            throw new InvalidRefreshTokenException(INVALID_REFRESH_TOKEN_MESSAGE);
        }
        String login = jwtService.extractLogin(request.getRefreshToken());
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE));
        return jwtService.generateToken(account);
    }

    @Override
    public Boolean validate(AccessTokenRequest accessTokenRequest) {
        return jwtService.validateAccessToken(accessTokenRequest.getAccessToken());
    }

    private void synchronizeRollback(Long userId) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status == STATUS_ROLLED_BACK) {
                            try {
                                apiClientService.deleteUser(userId);
                            } catch (Exception e) {
                                log.error("Compensation failed for userId {}: {}", userId, e.getMessage(), e);
                            }
                        }
                    }
                }
        );
    }

}