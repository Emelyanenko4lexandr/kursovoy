package dev.kursovoy.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class RegistrationRequest {
    private String fio;
    private String username;
    private String password;
}
