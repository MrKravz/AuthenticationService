package by.ares.authenticationservice.service.impl;

import by.ares.authenticationservice.model.Account;
import by.ares.authenticationservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static by.ares.authenticationservice.util.AuthServiceConstants.ACCOUNT_NOT_FOUND_MESSAGE;
import static by.ares.authenticationservice.util.AuthServiceConstants.ROLE_PREFIX;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String login) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE));
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(ROLE_PREFIX + account.getRole().name()));
            }

            @Override
            public @Nullable String getPassword() {
                return account.getPassword();
            }

            @Override
            public String getUsername() {
                return account.getLogin();
            }
        };
    }

}