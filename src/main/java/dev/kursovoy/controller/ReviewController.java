package dev.kursovoy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kursovoy.DTO.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/review")
@AllArgsConstructor
public class ReviewController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public ReviewDTO createReview(@RequestBody ReviewDTO reviewDTO) {

        // Преобразуем ReviewDTO в строку JSON с помощью библиотеки, например, Jackson
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String reviewJson = objectMapper.writeValueAsString(reviewDTO);

            // Отправляем строку в Kafka
            kafkaTemplate.send("review", reviewJson);

            return reviewDTO;
        } catch (JsonProcessingException e) {
            log.error("Error while serializing ReviewDTO", e);
            throw new RuntimeException("Error while serializing ReviewDTO", e);
        }
    }
}
