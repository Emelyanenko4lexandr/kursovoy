package dev.kursovoy.repository;


import dev.kursovoy.entity.Message;
import dev.kursovoy.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findByRecipient(User recipient);
}
