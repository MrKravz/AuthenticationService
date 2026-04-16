package by.ares.authenticationservice.service;

import by.ares.authenticationservice.dto.request.UserRequest;

public interface ApiClientService {

    Long createUser(UserRequest userRequest);
    void deleteUser(Long id);

}
