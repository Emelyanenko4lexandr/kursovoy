package dev.kursovoy;

import dev.kursovoy.entity.Credentials;
import dev.kursovoy.entity.Role;
import dev.kursovoy.entity.User;
import dev.kursovoy.repository.AutomobileRepository;
import dev.kursovoy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Initializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AutomobileRepository automobileRepository;

    public  void initial(){
        User user = new User(
                null,
                new Credentials("alexandr", "1234"),
                "Emelyanenko Alexandr",
                Role.USER,
                500.0,
                true
        );

        User adminUser = new User(
                null,
                new Credentials("admin", "admin"),
                "Admin User",
                Role.ADMIN,
                1000.10,
                true
        );

        userRepository.save(adminUser);
        userRepository.save(user);
    }
}
