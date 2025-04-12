package dev.kursovoy.DTO;

import dev.kursovoy.entity.CarStatus;

import java.time.Instant;

public record AutoResponse(
        Long id,
        String brand,
        String model,
        String registrationNumber,
        CarStatus status,
        Instant startRental) {
}
