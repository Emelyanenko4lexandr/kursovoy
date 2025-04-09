package dev.kursovoy.controller;

import dev.kursovoy.DTO.PhotoRequest;
import dev.kursovoy.DTO.PhotoResponse;
import dev.kursovoy.DTO.AutoIdRequest;
import dev.kursovoy.service.PhotoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/photo")
@AllArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping(value = "photos", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PhotoResponse> getAutoPhotoByPosition(@RequestBody PhotoRequest request) {
        return photoService.getAutoPhotoByPosition(request.getAutoId(), request.getPosition());
    }

    @PostMapping(value = "allphotos", produces = MediaType.APPLICATION_JSON_VALUE)
            //todo transactional read only??
    public List<PhotoResponse> getAutoPhotos(@RequestBody AutoIdRequest request) {
        return photoService.getAutoPhotos(request.getAutoId());
    }

}
