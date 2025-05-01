package dev.kursovoy.service;

import dev.kursovoy.entity.Automobile;
import dev.kursovoy.exception.NotFoundException;
import dev.kursovoy.repository.AutomobileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReviewListenerService {

    private final AutomobileRepository automobileRepository;

    @KafkaListener(
            topics = "average-rating",
            groupId = "groupId",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void setAverageRating(String review) {

        try {
            String[] parts = review.split(":");

            Long autoId = Long.parseLong(parts[0]);
            double averageRating = Double.parseDouble(parts[1]);

            Automobile auto = automobileRepository.findById(autoId)
                    .orElseThrow(() -> new NotFoundException("Car not found with ID: " + autoId));

            auto.setRating(averageRating);
            automobileRepository.save(auto);

        } catch (Exception e) {
            log.error("Failed to process Kafka message '{}': {}", review, e.getMessage(), e);
        }

    }
}
