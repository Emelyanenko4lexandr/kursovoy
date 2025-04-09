package dev.kursovoy.service;

import dev.kursovoy.DTO.UserResponse;
import dev.kursovoy.entity.User;
import dev.kursovoy.exception.NotFoundException;
import dev.kursovoy.mapper.UserMapper;
import dev.kursovoy.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String name) {

        User user = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserResponse userResponse = userMapper.userToUserResponse(user);

        return userResponse;
    }

    @Transactional(readOnly = true)
    public String getUserRole(String name) {

        User currentUser = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String userRole = currentUser.getRole().toString();

        return userRole;
    }

    public Double addBalance(Double sum, String name) {

        User currentUser = userRepository.findByCredUsername(name)
                .orElseThrow(() -> new NotFoundException("User not found"));

        //todo BAD_REQUEST exception
        if (sum == null || sum <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than zero");
        }

        currentUser.setBalance(currentUser.getBalance() + sum);

        userRepository.save(currentUser);

        return currentUser.getBalance();

    }
}
