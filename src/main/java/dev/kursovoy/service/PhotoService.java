package dev.kursovoy.service;

import dev.kursovoy.DTO.PhotoResponse;
import dev.kursovoy.entity.Photo;
import dev.kursovoy.mapper.PhotoMapper;
import dev.kursovoy.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;

    public List<PhotoResponse> getAutoPhotoByPosition(Long autoId, Integer position) {

        List<Photo> photos;

        if (position != null) {
            photos = photoRepository.findByAutomobileIdAndPosition(autoId, position);
        } else {
            photos = photoRepository.findByAutomobileId(autoId);
        }

        if (photos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No photos found for this automobile or position");
        }

        List<PhotoResponse> photoResponseList = photoMapper.toResponseList(photos);

        return photoResponseList;
    }

    public List<PhotoResponse> getAutoPhotos(Long autoId) {

        List<Photo> photos;

        photos = photoRepository.findByAutomobileId(autoId);

        if (photos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No photos found for this automobile or position");
        }

        List<PhotoResponse> photoResponseList = photoMapper.toResponseList(photos);

        return photoResponseList;
    }
}
