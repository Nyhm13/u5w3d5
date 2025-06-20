package it.epicode.u5w3d5.repository;

import it.epicode.u5w3d5.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Integer> {
}
