package dev.kursovoy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kursovoy.DTO.*;
import dev.kursovoy.entity.*;
import dev.kursovoy.exception.ConflictException;
import dev.kursovoy.exception.NotFoundException;
import dev.kursovoy.mapper.AutoMapper;
import dev.kursovoy.mapper.LocationMapper;
import dev.kursovoy.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Transactional
public class AutoService {

    private final AutomobileRepository automobileRepository;
    private final MessageRepository messageRepository;
    private final RentRepository rentRepository;

    private final AutoMapper autoMapper;
    private final LocationMapper locationMapper;

    private final PhotoService photoService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<AutoResponse> getFreeAuto() {

        List<Automobile> freeAutomobiles = automobileRepository.findByStatus(CarStatus.FREE);

        return autoMapper.toAutoResponseList(freeAutomobiles);
    }

    @Transactional(readOnly = true)
    public List<AllParametersDTO> getAllAuto() {

        List<Automobile> automobilesList = (List<Automobile>) automobileRepository.findAll();

        return autoMapper.toAllParametersDTOList(automobilesList);
    }

    public AutoResponse verifyAuto(Long autoId, String name) {

        Automobile autoOnVerifiction = automobileRepository.findByIdAndStatus(autoId, CarStatus.VERIFICATION)
                .orElseThrow(() -> new NotFoundException("Auto not found"));

        autoOnVerifiction.setStatus(CarStatus.FREE);

        automobileRepository.save(autoOnVerifiction);

        User sender = userService.getUserByUsername(name);

        String messageText = "Car: " + autoOnVerifiction.getBrand() + " " + autoOnVerifiction.getModel() +
                " has been successfully verified and can be rented";

        Message message = new Message(
                null,
                sender,
                autoOnVerifiction.getOwner(),
                MessageType.APPROVED,
                messageText
        );

        messageRepository.save(message);

        return autoMapper.toAutoResponse(autoOnVerifiction);
    }

    public AutoResponse deleteUnverifiedAuto(UnverifiedRequest request, String name) {

        Automobile auto = automobileRepository.findByIdAndStatus(request.getAutoId(), CarStatus.VERIFICATION)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auto not found"));

        AutoResponse autoResponse = autoMapper.toAutoResponse(auto);

        User sender = userService.getUserByUsername(name);

        String messageText = "Car: " + auto.getBrand() + " " + auto.getModel() + " was not verified";

        Message message = new Message(
                null,
                sender,
                auto.getOwner(),
                MessageType.DENIED,
                messageText,
                request.getReason()
        );

        automobileRepository.delete(auto);

        messageRepository.save(message);

        return autoResponse;
    }

    @Transactional(readOnly = true)
    public String getAuto(Long autoId) {

        Automobile auto = automobileRepository.findById(autoId)
                .orElseThrow(() -> new NotFoundException("Auto with ID " + autoId + " not found"));

        String response = auto.getBrand() + " " + auto.getModel();

        return response;
    }

    public Map<String, String> publishAuto(String publishedAutoJson, MultipartFile[] files, String username) {

        User owner = userService.getUserByUsername(username);

        try {
            // Преобразуем JSON строку в объект PublishAutoDTO
            ObjectMapper objectMapper = new ObjectMapper();
            PublishAutoDTO publishedAuto = objectMapper.readValue(publishedAutoJson, PublishAutoDTO.class);

            if (automobileRepository.findByRegistrationNumber(publishedAuto.getRegistration_number()).isPresent()) {
                throw new ConflictException("Registration number already exists");
            }

            Automobile automobile = new Automobile(
                    null,
                    publishedAuto.getBrand(),
                    publishedAuto.getModel(),
                    publishedAuto.getRegistration_number(),
                    CarStatus.VERIFICATION,
                    owner,
                    new Location(null, publishedAuto.getLatitude(), publishedAuto.getLongitude())
            );

            automobileRepository.save(automobile);

            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                Integer position = i + 1;

                photoService.uploadPhoto(file, username, automobile, position);

            }

            automobileRepository.save(automobile);

            // Возвращаем сообщение об успешной загрузке
            return Map.of("message", "Automobile with photos uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload automobile with photos", e);
        } catch (ResponseStatusException e) {
            throw e;
        }
    }

    public AutoResponse deleteAuto(Long autoId) {

        Automobile auto = automobileRepository.findByIdAndStatus(autoId, CarStatus.FREE)
                .orElseThrow(() -> new NotFoundException("Auto not found"));

        auto.setStatus(CarStatus.DELETED);

        automobileRepository.save(auto);

        return autoMapper.toAutoResponse(auto);
    }

    @Transactional(readOnly = true)
    public List<OwnerAutoResponse> getOwnerCars(String name) {

        User currentUser = userService.getUserByUsername(name);

        List<Automobile> ownerCars = automobileRepository.findByOwnerAndStatusNot(currentUser, CarStatus.DELETED);

        if (ownerCars.isEmpty()) {
            throw new NotFoundException("No cars found for the user");
        }

        return autoMapper.toOwnerAutoResponseList(ownerCars);
    }

    @Transactional(readOnly = true)
    public List<AutoResponse> getRentedAuto(String name) {

        User currentUser = userService.getUserByUsername(name);

        List<Rent> activeRents = rentRepository.findByTenantAndStatus(currentUser, RentStatus.ACTIVE);

        return autoMapper.toAutoResponseListFromRent(activeRents);
    }

    @Transactional(readOnly = true)
    public LocationResponse getCarLocation(Long autoId) {

        Automobile automobile = automobileRepository.findById(autoId)
                .orElseThrow(() -> new NotFoundException("Automobile not found"));

        Location location = automobile.getLocation();
        if(location == null) {
            throw new NotFoundException("Location not found foe this automobile");
        }

        return locationMapper.toLocationResponse(location);
    }
}

