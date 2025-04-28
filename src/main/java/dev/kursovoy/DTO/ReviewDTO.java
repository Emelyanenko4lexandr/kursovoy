package dev.kursovoy.DTO;

public record ReviewDTO (
        Long autoId,
        String senderUsername,
        String message
) {}
