package it.epicode.u5w3d5.service;

import it.epicode.u5w3d5.dto.EventDto;
import it.epicode.u5w3d5.exception.NotFoundException;
import it.epicode.u5w3d5.exception.PostiEsauritiException;
import it.epicode.u5w3d5.exception.UnAuthorizedException;
import it.epicode.u5w3d5.model.Event;
import it.epicode.u5w3d5.model.User;
import it.epicode.u5w3d5.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {



    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserService userService;


    // metodo per salvare un evento

    public Event saveEvent(EventDto eventDto) throws NotFoundException {
        User organizer= userService.getUser(eventDto.getOrganizerId());

        Event event= new Event();
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setEventDate(eventDto.getEventDate());
        event.setAvailableSeats(eventDto.getAvailableSeats());
        event.setOrganizer(organizer);

        return  eventRepository.save(event);
    }

    // metodo per  trovare un evento tramite id
    public Event getEvent(int id) throws NotFoundException {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Evento con id " + id + " non trovato"));
    }

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    public Event updateEvent(int id,EventDto eventDto) throws NotFoundException, PostiEsauritiException {
        Event eventoDaAggiornare=getEvent(id);

        int bookedSeats= eventoDaAggiornare.getReservations().size();

        if (eventoDaAggiornare.getAvailableSeats()<bookedSeats){
            throw new PostiEsauritiException("non puoi inserire meno posti disponibili rispetto a quelli già prenotati");
        }
        User user =userService.getUser(eventDto.getOrganizerId());

        if(eventoDaAggiornare.getOrganizer().getId()!=user.getId()){
            throw new UnAuthorizedException("non hai i permessi per modificare questo evento");
        }

        eventoDaAggiornare.setTitle(eventDto.getTitle());
        eventoDaAggiornare.setDescription(eventDto.getDescription());
        eventoDaAggiornare.setEventDate(eventDto.getEventDate());
        eventoDaAggiornare.setAvailableSeats(eventDto.getAvailableSeats());

        return eventRepository.save(eventoDaAggiornare);

    }
    public void deleteEvent(int id,int organizerId) throws NotFoundException {
        Event eventoDaCancellare=getEvent(id);
        if (eventoDaCancellare.getOrganizer().getId() != organizerId) {
            throw new UnAuthorizedException("Non hai i permessi per cancellare questo evento");
        }

        eventRepository.delete(eventoDaCancellare);
    }

}
