package dev.kursovoy.repository;

import dev.kursovoy.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByCredUsername(String username);
}
