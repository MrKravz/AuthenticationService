package by.ares.authenticationservice.unit.service;

import by.ares.authenticationservice.model.Account;
import by.ares.authenticationservice.repository.AccountRepository;
import by.ares.authenticationservice.service.AccountUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static by.ares.authenticationservice.util.TestConstants.*;
import static by.ares.authenticationservice.util.TestModelBuilder.buildAccount;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountUserDetailsServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountUserDetailsService userDetailsService;

    private Account account;

    @BeforeEach
    void init() {
        account = buildAccount();
    }

    @Test
    void shouldLoadUserSuccessfully() {
        when(accountRepository.findByLogin(LOGIN)).thenReturn(Optional.of(account));
        UserDetails userDetails = userDetailsService.loadUserByUsername(LOGIN);
        assertEquals(LOGIN, userDetails.getUsername());
        assertEquals(RAW_PASSWORD, userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals(userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority(), ROLE_PREFIX + ROLE);
        verify(accountRepository).findByLogin(LOGIN);
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserNotExists() {
        when(accountRepository.findByLogin(INVALID_LOGIN)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(INVALID_LOGIN));
        verify(accountRepository).findByLogin(INVALID_LOGIN);
    }

}
