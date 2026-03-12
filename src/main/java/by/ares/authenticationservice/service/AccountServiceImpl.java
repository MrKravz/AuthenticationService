package by.ares.authenticationservice.service;

import by.ares.authenticationservice.dto.request.AuthRequest;
import by.ares.authenticationservice.dto.request.RefreshTokenRequest;
import by.ares.authenticationservice.dto.request.RegisterRequest;
import by.ares.authenticationservice.dto.response.TokenDto;
import by.ares.authenticationservice.exception.AccountNotFoundException;
import by.ares.authenticationservice.exception.InvalidRefreshTokenException;
import by.ares.authenticationservice.exception.LoginAlreadyExistsException;
import by.ares.authenticationservice.mapper.SecurityMapper;
import by.ares.authenticationservice.model.Account;
import by.ares.authenticationservice.repository.AccountRepository;
import by.ares.authenticationservice.service.abstraction.AccountService;
import by.ares.authenticationservice.service.abstraction.ApiClientService;
import by.ares.authenticationservice.service.abstraction.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SecurityMapper securityMapper;
    private final ApiClientService apiClientService;

    private final String accountNotFoundMessage = "Account not found";
    private final String loginAlreadyExistsMessage = "This login already exists";
    private final String invalidRefreshTokenMessage = "Invalid refresh token";

    @Override
    public TokenDto register(RegisterRequest request) {
        accountRepository.findByLogin(request.getLogin()).orElseThrow(() -> {
            throw new LoginAlreadyExistsException(loginAlreadyExistsMessage);
        });
        Long userId = apiClientService.createUser(request.getUserRequest());
        Account account = securityMapper.toModel(request);
        account.setUserId(userId);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return jwtService.generateToken(account);
    }

    @Override
    public TokenDto authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(),
                request.getPassword()));
        Account account = accountRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new AccountNotFoundException(accountNotFoundMessage));
        return jwtService.generateToken(account);
    }

    @Override
    public TokenDto refreshToken(RefreshTokenRequest request) {
        if (!jwtService.validateToken(request.getRefreshToken())) {
            throw new InvalidRefreshTokenException(invalidRefreshTokenMessage);
        }
        String login = jwtService.extractLogin(request.getRefreshToken());
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException(accountNotFoundMessage));
        return jwtService.generateToken(account);
    }

}