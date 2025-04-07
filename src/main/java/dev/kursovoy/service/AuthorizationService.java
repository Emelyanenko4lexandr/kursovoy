package dev.kursovoy.service;

import dev.kursovoy.DTO.RegistrationRequest;
import dev.kursovoy.entity.Credentials;
import dev.kursovoy.entity.Role;
import dev.kursovoy.entity.User;
import dev.kursovoy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@AllArgsConstructor
public class AuthorizationService {

    private final UserRepository userRepository;

    //todo synchronized убрать, добавть transactional, aspect, response entity убрать
    public synchronized ResponseEntity<String> registerUser(RegistrationRequest request) {

        //todo controlleradvice
        if (userRepository.findByCredUsername(request.getUsername()).isPresent()) {
            //todo make custom exception
            throw new RuntimeException();
            //return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        Credentials credentials = new Credentials(request.getUsername(), request.getPassword());

        User user = new User(null, credentials, request.getFio(), Role.USER, 0.0, true);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    public synchronized UsernamePasswordAuthenticationToken apiLogin(Principal user) {
        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
        return token;
    }
}
