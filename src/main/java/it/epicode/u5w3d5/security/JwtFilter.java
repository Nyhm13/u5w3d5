package it.epicode.u5w3d5.security;

import it.epicode.u5w3d5.exception.NotFoundException;
import it.epicode.u5w3d5.exception.UnAuthorizedException;
import it.epicode.u5w3d5.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//possiamo copia incollare e modificare solo la parte
@Component
//questa classe è una classe astratta che estende OncePerRequestFilter, e permette di essere richiamata a ogni richiesta HTTP
//
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtTool jwtTool;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /* questo metodo viene chiamato per ogni richiesta
        e dovra verificare vari step:
        1. verificare se la richiesta ha il token
        2. se non ha il token, eccezione
        3. se ha il token possiamo verificare che il token sia valido e non scaduto. Se non e valido ,eccezione
        4. se il token è valido, allora si farà accedere la richiesta ai filtri successivi della catena
        */

        String authorization =request.getHeader("Authorization");
        // se questa authorizazzione non inizia con Bearer o e null, lanciamo una exception
        if (authorization == null || !authorization.startsWith("Bearer ")){
            throw  new UnAuthorizedException("Token non presente , non sei autorizzato ad usare il servizio richiesto");
        }
        else {
            // estraggo il token dalla stringa authorization che contiene anche la parola Bearer  prima del token. per questo prendo
            // solo la parte della stringa che comincia dal carattere 7
            // mi prendo il token, che è la parte dopo Bearer
            String token= authorization.substring(7);
            // verifico se il token è valido
            jwtTool.validateToken(token);
            try {
                User user = jwtTool.getUserFromToken(token);

                //import org.springframework.security.core.Authentication;
                Authentication authentication= new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (NotFoundException e){
                throw new UnAuthorizedException("Utente collegato al token non trovato, non sei autorizzato ad usare il servizio richiesto");
            }

            // se il token è valido, allora posso proseguire con la catena di filtri

            filterChain.doFilter(request,response);

        }
    }
    // questo metodo evita che gli endpoint di registrazione e login possano richiedere il token

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
