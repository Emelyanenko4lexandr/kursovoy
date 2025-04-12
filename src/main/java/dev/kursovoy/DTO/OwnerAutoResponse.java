package dev.kursovoy.DTO;

import dev.kursovoy.entity.CarStatus;

public record OwnerAutoResponse (
        Long id,
        String brand,
        String model,
        String registrationNumber,
        CarStatus status) {
}
