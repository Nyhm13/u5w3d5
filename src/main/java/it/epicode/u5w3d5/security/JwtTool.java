package it.epicode.u5w3d5.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.epicode.u5w3d5.exception.NotFoundException;
import it.epicode.u5w3d5.model.User;
import it.epicode.u5w3d5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // verra gestito in automatico da Spring
public class JwtTool {

    //classe gestita in automatico da Spring per la gestione dei token
    //dobbiamo creare un metodo per generare il token JWT

    @Value("${jwt.duration}")
    private long durata;
    @Value("${jwt.secret}")
    private String chiaveSegreta;

    @Autowired
    private UserService userService;


    public String createToken(User user){
        // per creare un token abbiamo bisogno della data di creazione, della durata del token e
        // del id dell`utente per il quale stiamo creando il token. Avremo inoltre bisogno anche della chiave segreta
        // per poter crittografare il token

        // per generare il token usiamo Jwts.builder() che ci permette di costruire il token con i vari metodi
        //issuedAt() per la data di creazione, expiration() per la durata del token,
        // subject() per il id dell`utente ,ricordati di concatenare user.getId+"" per renderlo una string , e signWith() per la chiave segreta
        // il metodo compact() serve per generare il token finale

        // ----------------------commenti pier
        // Crea la data di emissione (issuedAt) del token, ovvero il momento attuale
        // Calcola la data di scadenza sommando alla data corrente la durata (es. 15 minuti in ms)
        // In questo modo il token avrà una validità temporale ben definita

        // Il metodo builder() è statico e restituisce un builder JWT
        // Vengono impostati:
        // - issuedAt: data di creazione del token
        // - expiration: data di scadenza del token
        // - subject: identificativo dell’utente (in questo caso, il suo ID convertito in stringa)
        // - signWith: applica una firma HMAC usando la chiave segreta per garantire l’integrità del token
        // Infine, compact() serializza il token in formato stringa


        return Jwts.builder().issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+durata)).
                subject(user.getId()+"").
                signWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).compact();
    }

    // mi creo un metodo per la verifica della validità del token
    // controlla se il token è scaduto o se la firma non è valida controllando la firma del token con la chiave segreta

    public void validateToken(String token){
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).
                build().parse(token);
    }

    public User getUserFromToken(String token) throws NotFoundException {

        int id =Integer.parseInt(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).
                build().parseSignedClaims(token).getPayload().getSubject());

        return userService.getUser(id);
    }
}
