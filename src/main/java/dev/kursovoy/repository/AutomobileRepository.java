package dev.kursovoy.repository;

import dev.kursovoy.entity.Automobile;
import dev.kursovoy.entity.CarStatus;
import dev.kursovoy.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutomobileRepository extends CrudRepository<Automobile, Long> {
    List<Automobile> findByStatus(CarStatus status);

    @Query(value = "SELECT * FROM automobiles a WHERE a.owner_id = :ownerId AND a.status != 'DELETED'",
            nativeQuery = true)
    List<Automobile> findActiveOwnerCars(@Param("ownerId") Long ownerId);

    // Подумай над использованием @Query
    Optional<Automobile> findByIdAndStatus(Long id, CarStatus status);
    Optional<Automobile> findByRegistrationNumber(String registrationNumber);
}
