package by.ares.authenticationservice.exception;

public class LoginAlreadyExistsException extends RuntimeException {
    public LoginAlreadyExistsException(String message) {
        super(message);
    }
}
