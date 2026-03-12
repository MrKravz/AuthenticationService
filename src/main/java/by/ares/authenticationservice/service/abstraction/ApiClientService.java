package by.ares.authenticationservice.service.abstraction;

import by.ares.authenticationservice.dto.request.UserRequest;

public interface ApiClientService {

    Long createUser(UserRequest userRequest);

}
