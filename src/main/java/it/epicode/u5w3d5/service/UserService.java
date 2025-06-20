package it.epicode.u5w3d5.service;

import it.epicode.u5w3d5.dto.UserDto;
import it.epicode.u5w3d5.enumerations.Role;
import it.epicode.u5w3d5.exception.EntitaGiaEsistente;
import it.epicode.u5w3d5.exception.NotFoundException;
import it.epicode.u5w3d5.model.User;
import it.epicode.u5w3d5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(UserDto userDto) throws EntitaGiaEsistente {
        User user= new User();
        if (userRepository.existsByUsername(userDto.getUsername())){
            throw new EntitaGiaEsistente("Username gia esistente");
        }
        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new EntitaGiaEsistente("Email gia esistente");
        }

        user.setNome(userDto.getNome());
        user.setCognome(userDto.getCognome());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        user.setRole(Role.UTENTE);

        return  userRepository.save(user);

    }

    public List<User> getAllUser(){

        return userRepository.findAll();
    }

    public User getUser(int id) throws NotFoundException {
        return userRepository.findById(id).
                orElseThrow(() -> new NotFoundException("User con id " + id + " non trovato"));
    }

    public User updateUser(int id, UserDto userDto) throws NotFoundException {
        User userDaAggiornare = getUser(id);

        userDaAggiornare.setNome(userDto.getNome());
        userDaAggiornare.setCognome(userDto.getCognome());
        userDaAggiornare.setEmail(userDto.getEmail());
        if (!passwordEncoder.matches(userDto.getPassword(),userDaAggiornare.getPassword())){
        userDaAggiornare.setPassword(userDto.getPassword());
        }
        return userRepository.save(userDaAggiornare);

    }
    public void deleteUser(int id) throws NotFoundException {
        User userDaCancellare = getUser(id);

        userRepository.delete(userDaCancellare);
    }
}
