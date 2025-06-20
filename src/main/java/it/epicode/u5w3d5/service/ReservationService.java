package it.epicode.u5w3d5.service;

import it.epicode.u5w3d5.dto.ReservationDto;
import it.epicode.u5w3d5.exception.InvalidOperationException;
import it.epicode.u5w3d5.exception.NotFoundException;
import it.epicode.u5w3d5.exception.PostiEsauritiException;
import it.epicode.u5w3d5.model.Event;
import it.epicode.u5w3d5.model.Reservation;
import it.epicode.u5w3d5.model.User;
import it.epicode.u5w3d5.repository.EventRepository;
import it.epicode.u5w3d5.repository.ReservationRepository;
import it.epicode.u5w3d5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {


    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;


    public Reservation  salvaPrenotazione(ReservationDto reservationDto) throws NotFoundException, PostiEsauritiException {
        User user= userService.getUser(reservationDto.getUserId());
        Event event=eventService.getEvent(reservationDto.getEventID());
        // controllo se ci sono posti disponibili
        if (event.getAvailableSeats() <=0){
            throw new PostiEsauritiException("nessun posto disponibile per l`evento richiesto");
        }
        // controllo se l'utente ha già prenotato per l'evento
        boolean giaPrenotato= reservationRepository.existsByUserAndEvent(user, event);
        if (giaPrenotato){
            throw new NotFoundException("Utente con id " + user.getId() + " ha già prenotato per l'evento con id " + event.getId());
        }
        // se passano i due if allora creo la prenotazione
        Reservation reservation= new Reservation();

        reservation.setUser(user);
        reservation.setEvent(event);
        // tolgo dal numero di posti disponibili 1 posto perche l'utente ha prenotato
        event.setAvailableSeats(event.getAvailableSeats()-1);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }
    // restituisce tutte le prenotazioni di un utente
    public List<Reservation> getReservationsByUserId(int userId) throws NotFoundException {
        User user = userService.getUser(userId);
        return reservationRepository.findByUser(user);
    }

    public Reservation getReservation(int id) throws NotFoundException {
        return  reservationRepository.findById(id).orElseThrow(() -> new NotFoundException("la prenotazione con id " + id + " non è stata trovata"));
    }

    public Reservation updateReservation(int id , ReservationDto reservationDto) throws NotFoundException {
        Reservation prenotazioneDaAggiornare= getReservation(id);

        if(prenotazioneDaAggiornare.getUser().getId() != reservationDto.getUserId()){
            User user= userService.getUser(reservationDto.getUserId());
            prenotazioneDaAggiornare.setUser(user);
        }

        if(prenotazioneDaAggiornare.getEvent().getId() != reservationDto.getEventID()){
            Event evento= eventService.getEvent(reservationDto.getEventID());
            prenotazioneDaAggiornare.setEvent(evento);
        }
        return reservationRepository.save(prenotazioneDaAggiornare);

    }
    public void cancelReservation(int reservationId, int userId) throws NotFoundException, InvalidOperationException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Prenotazione non trovata con id " + reservationId));

        if (reservation.getUser().getId() != userId) {
            throw new InvalidOperationException("Non sei autorizzato ad annullare questa prenotazione");
        }

        Event event = reservation.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventRepository.save(event);

        reservationRepository.delete(reservation);
    }

}
