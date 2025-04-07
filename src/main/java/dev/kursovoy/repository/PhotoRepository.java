package dev.kursovoy.repository;

import dev.kursovoy.entity.Photo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhotoRepository extends CrudRepository<Photo, Long> {
    List<Photo> findByAutomobileId(Long automobileId);

    List<Photo> findByAutomobileIdAndPosition(Long automobileId, Integer position);
}
