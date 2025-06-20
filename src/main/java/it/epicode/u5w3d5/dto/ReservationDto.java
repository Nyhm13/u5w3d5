package it.epicode.u5w3d5.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationDto {
    @NotNull(message = "devi inserire l`id dell'utente che sta effettuando la prenotazione")
    private int userId;
    @NotNull(message = "devi inserire l`id dell'evento per cui si sta effettuando la prenotazione")
    private int eventID;
}
