package com.yasir.authService.services.Implementations;

import ch.qos.logback.classic.Logger;
import com.yasir.authService.models.UserDto;
import com.yasir.authService.services.EventProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducerServiceImpl implements EventProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    Logger log;

    public EventProducerServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produceEvent(UserDto userDto) {
        kafkaTemplate.send("user-data", userDto)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Successfully sent user event: {}", userDto.getEmail());
                    } else {
                        log.error("Failed to send user event to Kafka", ex);
                    }
                });
    }


}
