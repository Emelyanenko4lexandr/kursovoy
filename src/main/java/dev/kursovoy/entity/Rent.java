package dev.kursovoy.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "rents")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rent {

    public Rent(Long id, User tenant, Automobile auto, Instant startRental, RentStatus status) {
        this.id = id;
        this.tenant = tenant;
        this.auto = auto;
        this.startRental = startRental;
        this.status = status;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private User tenant;

    @OneToOne
    @JoinColumn(name = "auto_id", nullable = false)
    private Automobile auto;

    private Instant startRental;

    private Instant endRental;

    @Enumerated(EnumType.STRING)
    private RentStatus status;
}
