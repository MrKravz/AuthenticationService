package by.ares.authenticationservice.controller;


import by.ares.authenticationservice.dto.request.AuthRequest;
import by.ares.authenticationservice.dto.request.RefreshTokenRequest;
import by.ares.authenticationservice.dto.request.RegisterRequest;
import by.ares.authenticationservice.dto.response.TokenDto;
import by.ares.authenticationservice.service.abstraction.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;


    @PostMapping("/register")
    public ResponseEntity<TokenDto> register(@RequestBody RegisterRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(accountService.register(request));
    }

    @PostMapping("/token")
    public ResponseEntity<TokenDto> createToken(@RequestBody AuthRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.refreshToken(request));
    }

}
