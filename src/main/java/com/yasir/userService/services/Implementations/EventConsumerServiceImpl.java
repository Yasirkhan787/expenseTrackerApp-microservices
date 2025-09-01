package com.yasir.userService.services.Implementations;

import com.yasir.userService.entities.User;
import com.yasir.userService.models.UserDto;
import com.yasir.userService.repositories.UserRepository;
import com.yasir.userService.services.EventConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumerServiceImpl implements EventConsumerService {

    private final UserRepository userRepository;

    public EventConsumerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @KafkaListener(topics = "user-data", groupId = "user-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeEvent(UserDto userDto) {
        System.out.println("📩 Consumed User: " + userDto.getFullName() + " " + userDto.getEmail());

        // Map DTO -> Entity
        User user = User.builder()
                .id(userDto.getId())
                .fullName(userDto.getFullName())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        // Save to DB
        userRepository.save(user);

        System.out.println("✅ Saved user to DB: " + user.getFullName());
    }

}
