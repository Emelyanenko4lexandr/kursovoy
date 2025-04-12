package dev.kursovoy.service;

import dev.kursovoy.DTO.MessageResponse;
import dev.kursovoy.entity.Message;
import dev.kursovoy.entity.User;
import dev.kursovoy.mapper.MessageMapper;
import dev.kursovoy.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(String name) {

        User currentUser = userService.getUserByUsername(name);

        List<Message> messages = messageRepository.findByRecipient(currentUser);

        return messageMapper.toResponseList(messages);
    }

    public void deleteMessages(String name) {

        User currentUser = userService.getUserByUsername(name);

        List<Message> messages = messageRepository.findByRecipient(currentUser);

        messageRepository.deleteAll(messages);
    }
}
