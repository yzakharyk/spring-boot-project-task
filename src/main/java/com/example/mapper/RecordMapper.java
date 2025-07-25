package com.example.mapper;

import com.example.model.dto.RecordDto;
import org.springframework.stereotype.Component;
import com.example.model.entity.Record;

@Component
public class RecordMapper {

    public RecordDto toDto(Record record) {
        return new RecordDto(
                record.getUuid(),
                record.getName(),
                record.getStatus().toString(),
                record.getCreatedAt(),
                record.getCreatedBy(),
                record.getUpdatedAt(),
                record.getUpdatedBy()
        );
    }
}
