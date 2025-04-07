package dev.kursovoy.service;

import dev.kursovoy.DTO.MessageResponse;
import dev.kursovoy.entity.Message;
import dev.kursovoy.entity.User;
import dev.kursovoy.mapper.MessageMapper;
import dev.kursovoy.repository.MessageRepository;
import dev.kursovoy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    public List<MessageResponse> getMessages(String name) {

        User currentUser = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Message> messages = messageRepository.findByRecipient(currentUser);

        List<MessageResponse> messageResponseList = messageMapper.toResponseList(messages);


        return messageResponseList;
    }

    public void deleteMessages(String name) {

        User currentUser = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Message> messages = messageRepository.findByRecipient(currentUser);

        messageRepository.deleteAll(messages);
    }
}
