package by.ares.authenticationservice.service.impl;

import by.ares.authenticationservice.dto.request.UserRequest;
import by.ares.authenticationservice.exception.ExceptionResponse;
import by.ares.authenticationservice.exception.ExternalApiException;
import by.ares.authenticationservice.exception.ResponseParseException;
import by.ares.authenticationservice.service.ApiClientService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

import static by.ares.authenticationservice.util.AuthServiceConstants.RESPONSE_PARSE_MESSAGE;
import static by.ares.authenticationservice.util.AuthServiceConstants.SAVE_USER_URI;

@Service
@RequiredArgsConstructor
public class ApiClientServiceImpl implements ApiClientService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Override
    public Long createUser(UserRequest userRequest) {
        return restClient.post()
                .uri(SAVE_USER_URI)
                .body(userRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    ExceptionResponse error;
                    try (InputStream is = res.getBody()) {
                        error = objectMapper.readValue(is, ExceptionResponse.class);
                    } catch (IOException e) {
                        throw new ResponseParseException(RESPONSE_PARSE_MESSAGE);
                    }
                    throw new ExternalApiException(error.getMessage());
                })
                .body(Long.class);
    }

}

