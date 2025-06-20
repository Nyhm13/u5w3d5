package it.epicode.u5w3d5.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {
    @NotEmpty(message = "il campo username non puo essere nullo ")
    private String username;
    @NotEmpty(message = "il campo password non puo essere nullo")
    private String password;
    @NotEmpty(message = "il campo email non puo essere nullo")
    private String email;
}
