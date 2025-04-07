package dev.kursovoy.controller;

import dev.kursovoy.DTO.*;
import dev.kursovoy.entity.*;
import dev.kursovoy.mapper.AutoMapper;
import dev.kursovoy.mapper.RentMapper;
import dev.kursovoy.repository.AutomobileRepository;
import dev.kursovoy.repository.LocationRepository;
import dev.kursovoy.repository.RentRepository;
import dev.kursovoy.repository.UserRepository;
import dev.kursovoy.service.RentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/rent")
@AllArgsConstructor
public class RentController {

    private final RentService rentService;

    @GetMapping(value = "/allrents", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RentResponse> getAllRents() {
        return rentService.getAllRents();
    }

    @PutMapping(value = "/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public AutoResponse getRent(@RequestBody AutoIdRequest request, Principal principal) {
        return rentService.getRent(request.getAutoId(), principal.getName());
    }

    @PutMapping(value = "/rents", produces = MediaType.APPLICATION_JSON_VALUE)
    public AutoResponse endRent(@RequestBody EndRentReques request, Principal principal) {
        return rentService.endRent(
                principal.getName(),
                request.getAutoId(),
                request.getLatitude(),
                request.getLongitude());
    }

}
