package dev.kursovoy.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name= "credentials")
@Getter @Setter
@NoArgsConstructor
public class Credentials {

    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Credentials( String username, String password) {
        this.username = username;
        this.password = passwordEncoder.encode(password);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    @JsonIgnore
    private void setPasswordWithEncoding(String password) {this.password = passwordEncoder.encode(password);}

}
