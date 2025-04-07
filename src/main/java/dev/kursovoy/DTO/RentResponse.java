package dev.kursovoy.DTO;

import dev.kursovoy.entity.RentStatus;

import java.time.Instant;

public record RentResponse (
        Long id,
        String tenantName,
        String carBrand,
        String carModel,
        String carRegistrationNumber,
        Instant startRental,
        Instant endRental,
        RentStatus status) {
}
