package dev.kursovoy.service;

import dev.kursovoy.DTO.PhotoResponse;
import dev.kursovoy.entity.Automobile;
import dev.kursovoy.entity.Photo;
import dev.kursovoy.exception.ConflictException;
import dev.kursovoy.exception.NotFoundException;
import dev.kursovoy.mapper.PhotoMapper;
import dev.kursovoy.repository.PhotoRepository;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class PhotoService {

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;

    private final MinioClient minioClient = MinioClient
            .builder()
            .endpoint("http://localhost:9000")
            .credentials("minioadmin", "minioadmin")
            .build();

    public void uploadPhoto(MultipartFile file, String username, Automobile automobile, Integer position) {

        String bucketName = "minio-test-bucket";

        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());

            if (!found) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String object = username + "/" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("minio-test-bucket")
                            .object(object)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String photoUrl = String.format("http://localhost:9000/%s/%s", bucketName, object);

            Photo photo = new Photo(
                    photoUrl,
                    automobile,
                    position
            );
            photoRepository.save(photo);

            automobile.getPhotos().add(photo);

        } catch (Exception e) {
            throw new ConflictException("Failed to upload file to MinIO");
        }
    }

    @Transactional(readOnly = true)
    public List<PhotoResponse> getAutoPhotoByPosition(Long autoId, Integer position) {

        List<Photo> photos;

        if (position != null) {
            photos = photoRepository.findByAutomobileIdAndPosition(autoId, position);
        } else {
            photos = photoRepository.findByAutomobileId(autoId);
        }

        if (photos.isEmpty()) {
            throw new NotFoundException("No photos found for this automobile or position");
        }

        return photoMapper.toResponseList(photos);
    }

    @Transactional(readOnly = true)
    public List<PhotoResponse> getAutoPhotos(Long autoId) {

        List<Photo> photos;

        photos = photoRepository.findByAutomobileId(autoId);

        if (photos.isEmpty()) {
            throw new NotFoundException("No photos found for this automobile or position");
        }

        return photoMapper.toResponseList(photos);
    }
}
