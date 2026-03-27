package by.ares.authenticationservice.util;

public class AuthServiceConstants {
    public static final Long ACCESS_EXPIRATION = 1000L * 60 * 15;
    public static final Long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7;
    public static final String CLAIM_NAME_USER_ID = "userId";
    public static final String CLAIM_NAME_ROLE = "role";
    public static final String CLAIM_NAME_EXPIRATION_DATE = "exp";
    public static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String LOGIN_ALREADY_EXISTS_MESSAGE = "This login already exists";
    public static final String INVALID_REFRESH_TOKEN_MESSAGE = "Invalid refresh token";
    public static final String RESPONSE_PARSE_MESSAGE = "Failed to parse error response";

    private AuthServiceConstants() {
    }

}
