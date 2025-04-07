package dev.kursovoy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kursovoy.DTO.*;
import dev.kursovoy.entity.*;
import dev.kursovoy.exception.NotFoundException;
import dev.kursovoy.mapper.AutoMapper;
import dev.kursovoy.mapper.LocationMapper;
import dev.kursovoy.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AutoService {

    private final AutomobileRepository automobileRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final PhotoRepository photoRepository;
    private final RentRepository rentRepository;

    private final AutoMapper autoMapper;
    private final LocationMapper locationMapper;

    public List<AutoResponse> getFreeAuto() {

        List<Automobile> freeAutomobiles = automobileRepository.findByStatus(CarStatus.FREE);
        List<AutoResponse> AutoResponseList = autoMapper.toAutoResponseList(freeAutomobiles);

        return AutoResponseList;
    }

    public List<AllParametersDTO> getAllAuto() {

        List<Automobile> automobilesList = (List<Automobile>) automobileRepository.findAll();
        List<AllParametersDTO> AllParametersDTOList = autoMapper.toAllParametersDTOList(automobilesList);

        return AllParametersDTOList;

    }

    public AutoResponse verifyAuto(Long autoId, String name) {

        Automobile autoOnVerifiction = automobileRepository.findByIdAndStatus(autoId, CarStatus.VERIFICATION)
                .orElseThrow(() -> new NotFoundException("Auto not found"));

        autoOnVerifiction.setStatus(CarStatus.FREE);

        automobileRepository.save(autoOnVerifiction);

        User sender = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));

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

        AutoResponse autoResponse = autoMapper.toAutoResponse(autoOnVerifiction);

        return autoResponse;
    }

    public AutoResponse deleteUnverifiedAuto(UnverifiedRequest request, String name) {

        Automobile auto = automobileRepository.findByIdAndStatus(request.getAutoId(), CarStatus.VERIFICATION)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auto not found"));

        AutoResponse autoResponse = autoMapper.toAutoResponse(auto);

        User sender = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));

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

    public String getAuto(Long autoId) {

        Automobile auto = automobileRepository.findById(autoId)
                .orElseThrow(() -> new NotFoundException("Auto with ID " + autoId + " not found"));

        String response = auto.getBrand() + " " + auto.getModel();

        return response;
    }

    public Map<String, String> publishAuto(String publishedAutoJson, MultipartFile[] files, String username) {

        User owner = userRepository.findByCredUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        try {
            // Преобразуем JSON строку в объект PublishAutoDTO
            ObjectMapper objectMapper = new ObjectMapper();
            PublishAutoDTO publishedAuto = objectMapper.readValue(publishedAutoJson, PublishAutoDTO.class);

            //todo conflict exception
            if (automobileRepository.findByRegistrationNumber(publishedAuto.getRegistration_number()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Registration number already exists");
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

                // Преобразуем фото в Base64
                String base64Photo = "data:" + file.getContentType() + ";base64," +
                        Base64.getEncoder().encodeToString(file.getBytes());

                Photo photo = new Photo(
                        base64Photo,
                        automobile,
                        position
                );
                photoRepository.save(photo);

                automobile.getPhotos().add(photo);
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

        AutoResponse autoResponse = autoMapper.toAutoResponse(auto);

        return autoResponse;
    }

    public List<OwnerAutoResponse> getOwnerCars(String name) {

        User currentUser = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Automobile> ownerCars = automobileRepository.findByOwnerAndStatusNot(currentUser, CarStatus.DELETED);

        if (ownerCars.isEmpty()) {
            throw new NotFoundException("No cars found for the user");
        }

        List<OwnerAutoResponse> OwnerAutoResponseList = autoMapper.toOwnerAutoResponseList(ownerCars);

        return OwnerAutoResponseList;
    }

    public List<AutoResponse> getRentedAuto(String name) {

        User currentUser = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Rent> activeRents = rentRepository.findByTenant(currentUser).stream()
                .filter(rent -> rent.getStatus() == RentStatus.ACTIVE)
                .toList();

        List<AutoResponse> rentesCarList = autoMapper.toAutoResponseListFromRent(activeRents);

        return rentesCarList;
    }

    public LocationResponse getCarLocation(Long autoId) {

        Automobile automobile = automobileRepository.findById(autoId)
                .orElseThrow(() -> new NotFoundException("Automobile not found"));

        Location location = automobile.getLocation();
        if(location == null) {
            throw new NotFoundException("Location not found foe this automobile");
        }

        LocationResponse locationResponse = locationMapper.toLocationResponse(location);

        return locationResponse;
    }
}

