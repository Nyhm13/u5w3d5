package it.epicode.u5w3d5.service;

import it.epicode.u5w3d5.dto.LoginDto;
import it.epicode.u5w3d5.exception.NotFoundException;
import it.epicode.u5w3d5.model.User;
import it.epicode.u5w3d5.repository.UserRepository;
import it.epicode.u5w3d5.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String login (LoginDto loginDto) throws NotFoundException {
        User user =userRepository.findByUsernameAndEmail(loginDto.getUsername(),loginDto.getEmail()).
                orElseThrow(() -> new NotFoundException("Utente con questo username/email non trovato"));

        if (passwordEncoder.matches(loginDto.getPassword(),user.getPassword())){
            return jwtTool.createToken(user);
        } else {
            throw  new NotFoundException("Utente con questo username/password non trovato");
        }
    }
}
