package by.ares.authenticationservice.util;

import by.ares.authenticationservice.model.Role;

import java.time.LocalDate;

public class TestConstants {

    public static final Long ACCOUNT_ID = 1L;
    public static final String SECRET = "mySuperSecretKeyForJwtGenerationThatIsLongEnough";
    public static final String LOGIN = "testUser";
    public static final String INVALID_LOGIN = "invalidLogin";
    public static final Long USER_ID = 2L;
    public static final Role ROLE = Role.USER;
    public static final String ENCODED_PASSWORD = "encodedPassword";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String RAW_PASSWORD = "rawPassword";
    public static final String ACCESS_TOKEN = "access";
    public static final String INVALID_REFRESH_TOKEN = "invalidToken";
    public static final String VALID_REFRESH_TOKEN = "validToken";
    public static final String NAME = "Nick";
    public static final String SURNAME = "Fury";
    public static final String EMAIL = "email@gmail.com";
    public static final LocalDate BIRTH_DATE = LocalDate.of(2000,1,1);

}
