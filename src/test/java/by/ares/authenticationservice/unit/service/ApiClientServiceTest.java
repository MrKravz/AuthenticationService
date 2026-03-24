package by.ares.authenticationservice.unit.service;

import by.ares.authenticationservice.dto.request.UserRequest;
import by.ares.authenticationservice.service.impl.ApiClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import static by.ares.authenticationservice.util.TestConstants.URI;
import static by.ares.authenticationservice.util.TestConstants.USER_ID;
import static by.ares.authenticationservice.util.TestModelBuilder.buildUserRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiClientServiceTest {

    @Mock
    private RestClient restClient;
    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private RestClient.RequestBodySpec requestBodySpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private ApiClientServiceImpl apiClientService;

    private UserRequest userRequest;


    @BeforeEach
    void init() {
        userRequest = buildUserRequest();
        ReflectionTestUtils.setField(apiClientService, "uri", URI);
    }


    @Test
    void shouldCreateUserSuccessfully() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(UserRequest.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(Long.class)).thenReturn(USER_ID);
        Long result = apiClientService.createUser(userRequest);
        assertEquals(USER_ID, result);
    }

}
