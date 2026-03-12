package by.ares.authenticationservice.service;

import by.ares.authenticationservice.dto.request.UserRequest;
import by.ares.authenticationservice.exception.ExceptionResponse;
import by.ares.authenticationservice.exception.ExternalApiException;
import by.ares.authenticationservice.exception.ResponseParseException;
import by.ares.authenticationservice.service.abstraction.ApiClientService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ApiClientServiceImpl implements ApiClientService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    private final String saveUserUri = "";
    private final String responseParseMessage = "Failed to parse error response";

    @Override
    public Long createUser(UserRequest userRequest) {
        return restClient.post()
                .uri(saveUserUri)
                .body(userRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    ExceptionResponse error;
                    try (InputStream is = res.getBody()) {
                        error = objectMapper.readValue(is, ExceptionResponse.class);
                    } catch (IOException e) {
                        throw new ResponseParseException(responseParseMessage);
                    }
                    throw new ExternalApiException(error.getMessage());
                })
                .body(Long.class); // TODO refactor
    }

}

