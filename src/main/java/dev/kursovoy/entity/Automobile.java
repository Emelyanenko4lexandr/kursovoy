package dev.kursovoy.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "automobiles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Automobile {


    public Automobile(Long id, String brand, String model, String registrationNumber,
                      CarStatus status, Double rating, User owner, Location location) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.status = status;
        this.rating = rating;
        this.owner = owner;
        this.location = location;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String brand;

    private String model;

    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    private Double rating;

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToOne
    @JoinColumn(name = "rent_id")
    private Rent rent;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "automobile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Photo> photos = new ArrayList<>();

}
