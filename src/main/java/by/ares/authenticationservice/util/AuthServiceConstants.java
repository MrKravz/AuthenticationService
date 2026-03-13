package by.ares.authenticationservice.util;

public class AuthServiceConstants {
    public static final long accessExpiration = 1000 * 60 * 15;
    public static final long refreshExpiration = 1000L * 60 * 60 * 24 * 7;
    public static final String claimNameUserId = "userId";
    public static final String claimNameRole = "role";
    public static final String accountNotFoundMessage = "Account not found";
    public static final String rolePrefix = "ROLE_";
    public static final String loginAlreadyExistsMessage = "This login already exists";
    public static final String invalidRefreshTokenMessage = "Invalid refresh token";
    public static final String saveUserUri = "http://localhost:8080/users";
    public static final String responseParseMessage = "Failed to parse error response";
}
