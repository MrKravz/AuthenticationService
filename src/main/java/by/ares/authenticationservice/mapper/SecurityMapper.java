package by.ares.authenticationservice.mapper;

import by.ares.authenticationservice.dto.request.RegisterRequest;
import by.ares.authenticationservice.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SecurityMapper {
    Account toModel(RegisterRequest request);
}
