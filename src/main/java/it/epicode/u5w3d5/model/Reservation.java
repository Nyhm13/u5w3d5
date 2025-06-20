package it.epicode.u5w3d5.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
