package dev.kursovoy.controller;

import dev.kursovoy.DTO.UserResponse;
import dev.kursovoy.entity.User;
import dev.kursovoy.mapper.UserMapper;
import dev.kursovoy.repository.UserRepository;
import dev.kursovoy.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/current-user")
    public UserResponse getCurrentUser(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return userService.getCurrentUser(principal.getName());
    }

    @GetMapping(value = "/userrole", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserRole(Principal principal) {
        return userService.getUserRole(principal.getName());
    }

    @PostMapping(value = "current-user/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public Double addBalance(@RequestBody Map<String, Double> requestBody, Principal principal) {

        Double sum = requestBody.get("sum");

        return userService.addBalance(sum, principal.getName());
    }

}
