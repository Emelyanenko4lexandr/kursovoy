package dev.kursovoy.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class User {
    public User(Long id, Credentials credentials, String fio, Role role, Double balance, boolean enable) {
        this.id = id;
        this.cred = credentials;
        this.fio = fio;
        this.role = role;
        this.balance = balance;
        this.enable = enable;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "credentials_id", nullable = false)
    private Credentials cred;

    private String fio;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Double balance;

    private boolean enable;
}
