package com.ex.producer;

public record MessageDto(
        Long id,
        String message,
        Long length
) {
}
