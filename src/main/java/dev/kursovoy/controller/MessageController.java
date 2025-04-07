package dev.kursovoy.controller;

import dev.kursovoy.DTO.MessageResponse;
import dev.kursovoy.entity.Message;
import dev.kursovoy.entity.User;
import dev.kursovoy.mapper.MessageMapper;
import dev.kursovoy.repository.MessageRepository;
import dev.kursovoy.repository.UserRepository;
import dev.kursovoy.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/message")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageResponse> getMessages(Principal principal) {
        return messageService.getMessages(principal.getName());
    }

    @DeleteMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMessages(Principal principal) {

        messageService.deleteMessages(principal.getName());

        return ResponseEntity.ok().build();
    }
}
