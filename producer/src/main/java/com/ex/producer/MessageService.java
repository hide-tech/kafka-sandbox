package com.ex.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService implements MessageProducer{
    private final KafkaTemplate<Long, MessageDto> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(initialDelay = 10000, fixedDelay = 500)
    @Override
    public void produce() {
        MessageDto message = createMessage();
        log.info("<= sending {}", writeValueAsString(message));
        kafkaTemplate.send("input", message);
    }

    private MessageDto createMessage() {
        return new MessageDto(new Random().nextLong(), "message from producer",
                new Random().nextLong());
    }

    private String writeValueAsString(MessageDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Writing value to JSON failed: " + dto.toString());
        }
    }
}
