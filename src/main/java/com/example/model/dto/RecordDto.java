package com.example.model.dto;

import java.time.LocalDateTime;

public record RecordDto(
        String uuid,
        String name,
        String status,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy) {
}

