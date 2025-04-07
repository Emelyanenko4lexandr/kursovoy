package dev.kursovoy.repository;

import dev.kursovoy.entity.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {
}
