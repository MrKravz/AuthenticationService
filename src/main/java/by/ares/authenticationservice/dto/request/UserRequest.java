package by.ares.authenticationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
}
