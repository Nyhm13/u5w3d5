package it.epicode.u5w3d5.repository;

import it.epicode.u5w3d5.model.Event;
import it.epicode.u5w3d5.model.Reservation;
import it.epicode.u5w3d5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {

    boolean existsByUserAndEvent(User user, Event event);
    List<Reservation> findByUser(User user);

}
