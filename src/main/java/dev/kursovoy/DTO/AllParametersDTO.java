package dev.kursovoy.DTO;

import dev.kursovoy.entity.CarStatus;

public record AllParametersDTO (
        Long id,
        String brand,
        String model,
        String registrationNumber,
        CarStatus status,
        String owner,
        String tenant,
        Double latitude,
        Double longitude){
}
