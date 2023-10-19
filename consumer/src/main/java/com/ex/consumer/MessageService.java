package com.ex.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService implements MessageConsumer{
    private final ObjectMapper objectMapper;

    @KafkaListener(id = "message", topics = {"output"}, containerFactory = "singleFactory")
    @Override
    public void consume(MessageDto messageDto) {
        log.info("=> consumed {}", writeValueAsString(messageDto));
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
