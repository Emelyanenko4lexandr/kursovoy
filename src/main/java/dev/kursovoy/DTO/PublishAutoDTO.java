package dev.kursovoy.DTO;

import lombok.Getter;

import java.nio.channels.MulticastChannel;

@Getter
public class PublishAutoDTO {
    private String brand;
    private String model;
    private String registration_number;
    private Double latitude;
    private Double longitude;
    private MulticastChannel photo;
}
