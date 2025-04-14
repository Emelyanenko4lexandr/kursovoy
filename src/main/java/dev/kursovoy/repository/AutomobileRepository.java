package dev.kursovoy.repository;

import dev.kursovoy.entity.Automobile;
import dev.kursovoy.entity.CarStatus;
import dev.kursovoy.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AutomobileRepository extends CrudRepository<Automobile, Long> {
    List<Automobile> findByStatus(CarStatus status);
    List<Automobile> findByOwnerAndStatusNot(User owner, CarStatus status);

    // Подумай над использованием @Query
    Optional<Automobile> findByIdAndStatus(Long id, CarStatus status);
    Optional<Automobile> findByRegistrationNumber(String registrationNumber);
}
