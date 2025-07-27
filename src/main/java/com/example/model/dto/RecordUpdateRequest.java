package com.example.model.dto;

import com.example.model.entity.Record;

public record RecordUpdateRequest(
        String name,
        Record.Status status) {
}
