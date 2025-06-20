package it.epicode.u5w3d5.controller;

import it.epicode.u5w3d5.dto.LoginDto;
import it.epicode.u5w3d5.dto.UserDto;
import it.epicode.u5w3d5.exception.EntitaGiaEsistente;
import it.epicode.u5w3d5.exception.NotFoundException;
import it.epicode.u5w3d5.exception.ValidationException;
import it.epicode.u5w3d5.model.User;
import it.epicode.u5w3d5.service.AuthService;
import it.epicode.u5w3d5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/register")
    public User register(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws ValidationException, EntitaGiaEsistente {

        if (bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("",(e,s)-> e+s));
        }
       return userService.saveUser(userDto);
    }

    @GetMapping("/auth/login")
    public String login (@RequestBody @Validated LoginDto loginDto, BindingResult bindingResult) throws ValidationException, NotFoundException {
        if (bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("",(e,s)-> e+s));
        }

        // questi step verranno gestiti da AuthService
        /* 1 passaggio: verifico se l`utente esiste nel database
           2 passaggio: se l`utente non esiste, lancia una eccezione
           3 passaggio: se l`utente esiste , generare il token e inviarlo al client
         */


        return authService.login(loginDto);


    }

}
