package it.epicode.u5w3d5.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {
    @NotEmpty(message = "il campo nome non puo essere nullo ")
    private String nome;
    @NotEmpty(message = "il campo nome non puo essere nullo ")
    private String cognome;
    @NotEmpty(message = "il campo username non puo essere vuoto ")
    private String username;
    @NotEmpty(message = "il campo password non puo essere vuoto ")
    private String password;
    @Email(message = "il campo email deve avere un indirizzo email valido")
    private String email;

}
