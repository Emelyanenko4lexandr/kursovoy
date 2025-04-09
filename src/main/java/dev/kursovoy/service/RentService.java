package dev.kursovoy.service;

import dev.kursovoy.DTO.AutoResponse;
import dev.kursovoy.DTO.RentResponse;
import dev.kursovoy.entity.*;
import dev.kursovoy.exception.NotFoundException;
import dev.kursovoy.mapper.AutoMapper;
import dev.kursovoy.mapper.RentMapper;
import dev.kursovoy.repository.AutomobileRepository;
import dev.kursovoy.repository.LocationRepository;
import dev.kursovoy.repository.RentRepository;
import dev.kursovoy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class RentService {

    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final AutomobileRepository automobileRepository;
    private final LocationRepository locationRepository;

    private final RentMapper rentMapper;
    private final AutoMapper autoMapper;

    @Transactional(readOnly = true)
    public List<RentResponse> getAllRents() {

        List<Rent> allRents = (List<Rent>) rentRepository.findAll();

        if (allRents.isEmpty()) {
            throw new NotFoundException("Rents not found");
        }

        List<RentResponse> rentResponseList = rentMapper.toRentResponseList(allRents);

        return rentResponseList;
    }

    public AutoResponse getRent(Long autoId, String name) {

        User currentUser = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Automobile auto = automobileRepository.findByIdAndStatus(autoId, CarStatus.FREE)
                .orElseThrow(() -> new NotFoundException("Automobile not found"));

        //todo BAD_REQUEST exception
        if (auto.getRent() != null || auto.getStatus() != CarStatus.FREE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The car cannot be rented");
        } else {
            Rent rent = new Rent(
                    null,
                    currentUser,
                    auto,
                    Instant.now(),
                    RentStatus.ACTIVE
            );

            rentRepository.save(rent);
            auto.setRent(rent);
            auto.setStatus(CarStatus.USED);
            automobileRepository.save(auto);

            AutoResponse rentedCar = autoMapper.toAutoResponse(auto);

            return rentedCar;
        }
    }

    public AutoResponse endRent(String name, Long autoId, Double latitude, Double longitude) {

        User currentUser = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Automobile auto = automobileRepository.findById(autoId)
                .orElseThrow(() -> new NotFoundException("Automobile not found"));

        Rent rent = rentRepository.findByAutoAndTenantAndStatus(auto, currentUser, RentStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("Rent not found"));

        Location newLocation = new Location(null, latitude, longitude);

        Location oldLocation = auto.getLocation();

        if (latitude != null && longitude != null) {
            auto.setRent(null);
            auto.setStatus(CarStatus.FREE);
            auto.setLocation(newLocation);
            automobileRepository.save(auto);

            rent.setStatus(RentStatus.FINISHED);
            rent.setEndRental(Instant.now());
            rentRepository.save(rent);

            if (oldLocation != null) {
                locationRepository.delete(oldLocation); // Удаляем старое местоположение
            }
        }

        AutoResponse releasedCar = autoMapper.toAutoResponse(auto);

        return releasedCar;
    }
}
