package by.ares.authenticationservice.service;

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

import java.util.Collection;
import java.util.List;

import static by.ares.authenticationservice.util.AuthServiceConstants.accountNotFoundMessage;
import static by.ares.authenticationservice.util.AuthServiceConstants.rolePrefix;

@Service
@RequiredArgsConstructor
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;



    @Override
    public UserDetails loadUserByUsername(String login) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(accountNotFoundMessage));
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(rolePrefix + account.getRole().name()));
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