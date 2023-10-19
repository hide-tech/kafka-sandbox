package com.ex.consumer;

public record MessageDto(
        Long id,
        String message,
        Long length
) {
}
