package dev.kursovoy.service;

import dev.kursovoy.DTO.AutoResponse;
import dev.kursovoy.DTO.RentResponse;
import dev.kursovoy.entity.*;
import dev.kursovoy.exception.BadRequestException;
import dev.kursovoy.exception.NotFoundException;
import dev.kursovoy.mapper.AutoMapper;
import dev.kursovoy.mapper.RentMapper;
import dev.kursovoy.repository.AutomobileRepository;
import dev.kursovoy.repository.LocationRepository;
import dev.kursovoy.repository.RentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class RentService {

    private final RentRepository rentRepository;
    private final AutomobileRepository automobileRepository;
    private final LocationRepository locationRepository;

    private final RentMapper rentMapper;
    private final AutoMapper autoMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<RentResponse> getAllRents() {

        List<Rent> allRents = (List<Rent>) rentRepository.findAll();

        if (allRents.isEmpty()) {
            throw new NotFoundException("Rents not found");
        }

        return rentMapper.toRentResponseList(allRents);
    }

    public AutoResponse getRent(Long autoId, String name) {

        User currentUser = userService.getUserByUsername(name);

        Automobile auto = automobileRepository.findByIdAndStatus(autoId, CarStatus.FREE)
                .orElseThrow(() -> new NotFoundException("Automobile not found"));

        //todo BAD_REQUEST exception
        if (auto.getRent() != null || auto.getStatus() != CarStatus.FREE) {
            throw new BadRequestException("The car cannot be rented");
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

            return autoMapper.toAutoResponse(auto);
        }
    }

    public AutoResponse endRent(Long autoId, Double latitude, Double longitude) {

        Automobile auto = automobileRepository.findById(autoId)
                .orElseThrow(() -> new NotFoundException("Automobile not found"));

        Location newLocation = new Location(null, latitude, longitude);

        if (latitude != null && longitude != null) {
            auto.setRent(null);
            auto.setLocation(newLocation);
            automobileRepository.save(auto);
        }

        return autoMapper.toAutoResponse(auto);
    }
}
