package com.ex.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfig {
    @Value("${kafka.server}")
    private String kafkaServer;
    @Value("${kafka.group.id}")
    private String kafkaGroupId;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaGroupId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public Serde<MessageDto> messageSerde() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(MessageDto.class));
    }

    @Bean
    public KStream<Long, MessageDto> kStream(StreamsBuilder kStreamBuilder) {
        KStream<Long, String> stream = kStreamBuilder
                .stream("input", Consumed.with(Serdes.Long(), Serdes.String()));
        KStream<Long, MessageDto> messageStream = stream
                .mapValues(this::getUserFromString)
                .filter((key, value) -> value.length() >= 0);
        messageStream.to("output", Produced.with(Serdes.Long(), messageSerde()));
        return messageStream;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    MessageDto getUserFromString(String messageString) {
        MessageDto messageDto = null;
        try {
            messageDto = objectMapper().readValue(messageString, MessageDto.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return messageDto;
    }
}
