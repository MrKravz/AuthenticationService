package by.ares.authenticationservice.service;

import by.ares.authenticationservice.dto.request.AuthRequest;
import by.ares.authenticationservice.dto.request.RefreshTokenRequest;
import by.ares.authenticationservice.dto.request.RegisterRequest;
import by.ares.authenticationservice.dto.response.TokenDto;
import by.ares.authenticationservice.exception.AccountNotFoundException;
import by.ares.authenticationservice.exception.InvalidRefreshTokenException;
import by.ares.authenticationservice.exception.LoginAlreadyExistsException;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import static by.ares.authenticationservice.util.AuthServiceConstants.*;

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
            throw new LoginAlreadyExistsException(loginAlreadyExistsMessage);
        }
        Long userId = apiClientService.createUser(request.getUserRequest());
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
                .orElseThrow(() -> new AccountNotFoundException(accountNotFoundMessage));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(),
                request.getPassword()));
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