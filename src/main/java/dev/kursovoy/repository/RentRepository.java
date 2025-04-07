package dev.kursovoy.repository;

import dev.kursovoy.entity.Automobile;
import dev.kursovoy.entity.Rent;
import dev.kursovoy.entity.RentStatus;
import dev.kursovoy.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RentRepository extends CrudRepository<Rent, Long> {
    List<Rent> findByTenant(User tenant);

    Optional<Rent> findByAutoAndTenantAndStatus(Automobile auto, User tenant, RentStatus status);
}
