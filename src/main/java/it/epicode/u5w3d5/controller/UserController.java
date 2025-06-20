package it.epicode.u5w3d5.controller;

import it.epicode.u5w3d5.dto.UserDto;
import it.epicode.u5w3d5.exception.NotFoundException;
import it.epicode.u5w3d5.exception.ValidationException;
import it.epicode.u5w3d5.model.User;
import it.epicode.u5w3d5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) throws NotFoundException {
        return userService.getUser(id);
    }

    @GetMapping("")
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws ValidationException, NotFoundException {
        if (bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).reduce("",(e,s)->e+s));
        }
        return  userService.updateUser(id, userDto);
    }

    @PatchMapping("/{id}")
    public User aggiornamentoUser(@PathVariable int id, @RequestBody UserDto userDto) throws NotFoundException {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) throws NotFoundException {
        userService.deleteUser(id);
    }
}
