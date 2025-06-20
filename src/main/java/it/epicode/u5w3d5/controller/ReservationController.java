package it.epicode.u5w3d5.controller;

import it.epicode.u5w3d5.dto.ReservationDto;
import it.epicode.u5w3d5.exception.NotFoundException;
import it.epicode.u5w3d5.exception.PostiEsauritiException;
import it.epicode.u5w3d5.exception.ValidationException;
import it.epicode.u5w3d5.model.Reservation;
import it.epicode.u5w3d5.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {


    @Autowired
    private ReservationService reservationService;



    @PostMapping("")
    public Reservation createReservation(@RequestBody @Validated ReservationDto reservationDto, BindingResult bindingResult) throws ValidationException, NotFoundException, PostiEsauritiException {
        if (bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).reduce("",(e, s)->e+s));
        }
        return reservationService.salvaPrenotazione(reservationDto);
    }

    @GetMapping("/{id}")
    public Reservation getReservation(@PathVariable int id) throws NotFoundException {
        return  reservationService.getReservation(id);
    }
    @GetMapping("")
    public List<Reservation> getListaPrenotazioni(){
        return  reservationService.getAllReservations();
    }
    @GetMapping("/user/{userId}")
    public List<Reservation> getReservationsByUser(@PathVariable int userId) throws NotFoundException {
        return reservationService.getReservationsByUserId(userId);
    }

    @PutMapping("/{id}")
    public Reservation updateReservation(@PathVariable int id ,@RequestBody @Validated ReservationDto reservationDto, BindingResult bindingResult) throws ValidationException, NotFoundException {
        if (bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).reduce("",(e, s)->e+s));
        }
        return reservationService.updateReservation(id, reservationDto);
    }
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable int id,@RequestParam int userId) throws NotFoundException {
        reservationService.cancelReservation(id, userId);
    }


}
