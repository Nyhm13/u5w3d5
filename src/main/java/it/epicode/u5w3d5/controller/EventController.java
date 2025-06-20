package it.epicode.u5w3d5.controller;

import it.epicode.u5w3d5.dto.EventDto;
import it.epicode.u5w3d5.dto.ReservationDto;
import it.epicode.u5w3d5.exception.NotFoundException;
import it.epicode.u5w3d5.exception.PostiEsauritiException;
import it.epicode.u5w3d5.exception.ValidationException;
import it.epicode.u5w3d5.model.Event;
import it.epicode.u5w3d5.model.Reservation;
import it.epicode.u5w3d5.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;


    @PostMapping("")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Event createEvent(@RequestBody @Validated EventDto eventDto, BindingResult bindingResult) throws ValidationException, NotFoundException {
        if (bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).reduce("",(e, s)->e+s));
        }
        return eventService.saveEvent(eventDto);
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable int id) throws NotFoundException {
        return  eventService.getEvent(id);
    }
    @GetMapping("")
    public List<Event> getListaEvent(){
        return  eventService.getAllEvents();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Event updateEvent(@PathVariable int id ,@RequestBody @Validated EventDto eventDto, BindingResult bindingResult) throws ValidationException, NotFoundException, PostiEsauritiException {
        if (bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).reduce("",(e, s)->e+s));
        }
        return eventService.updateEvent(id, eventDto);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public void deleteEvent(@PathVariable int id,@RequestParam int userId) throws NotFoundException {
        eventService.deleteEvent(id, userId);
    }
}
