package it.epicode.u5w3d5.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventDto {
    @NotEmpty(message = "devi inserire un titolo per l`evento")
    private String title;
    @NotEmpty(message = "devi inserire una descrizione dell evento")
    private String description;
    @NotNull(message = "il campo non puo essere nullo devi inserire la data dell'evento")
    private LocalDate eventDate;
    @NotNull(message = "il campo non puo essere nullo devi inserire il numero di posti disponibili")
    private int availableSeats;

    @NotNull(message = "devi inserire l`id del organizzatore dell'evento")
    private int organizerId;
}
