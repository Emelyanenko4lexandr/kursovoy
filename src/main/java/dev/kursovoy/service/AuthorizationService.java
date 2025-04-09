package dev.kursovoy.service;

import dev.kursovoy.DTO.RegistrationRequest;
import dev.kursovoy.entity.Credentials;
import dev.kursovoy.entity.Role;
import dev.kursovoy.entity.User;
import dev.kursovoy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@AllArgsConstructor
@Transactional
public class AuthorizationService {

    private final UserRepository userRepository;

    //todo synchronized убрать, добавть transactional, aspect, response entity убрать
    public String registerUser(RegistrationRequest request) {

        //todo controlleradvice
        if (userRepository.findByCredUsername(request.getUsername()).isPresent()) {
            //todo make custom exception
            throw new RuntimeException("Username already exists");
        }

        Credentials credentials = new Credentials(request.getUsername(), request.getPassword());

        User user = new User(null, credentials, request.getFio(), Role.USER, 0.0, true);

        userRepository.save(user);

        return "User registered successfully";
    }

    public UsernamePasswordAuthenticationToken apiLogin(Principal user) {
        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
        return token;
    }
}
