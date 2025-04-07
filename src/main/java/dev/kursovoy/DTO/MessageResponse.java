package dev.kursovoy.DTO;

import dev.kursovoy.entity.MessageType;

public record MessageResponse (
        Long id,
        String senderUsername,
        String recipientUsername,
        MessageType type,
        String message,
        String reason
) {
}
