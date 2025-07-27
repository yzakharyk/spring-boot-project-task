package com.example.mapper;

import com.example.model.dto.RecordDto;
import com.example.model.entity.Record;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RecordMapperTest {

    private final RecordMapper recordMapper = new RecordMapper();

    @Test
    void testToDto() {
        // Arrange
        Record record = new Record();
        record.setUuid("123e4567-e89b-12d3-a456-426614174000");
        record.setName("Test Record");
        record.setStatus(Record.Status.ACTIVE);
        record.setCreatedAt(LocalDateTime.of(2023, 7, 25, 10, 0));
        record.setCreatedBy("admin");
        record.setUpdatedAt(LocalDateTime.of(2023, 7, 25, 12, 0));
        record.setUpdatedBy("admin");

        // Act
        RecordDto recordDto = recordMapper.toDto(record);

        // Assert
        assertNotNull(recordDto);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", recordDto.uuid());
        assertEquals("Test Record", recordDto.name());
        assertEquals("ACTIVE", recordDto.status());
        assertEquals(LocalDateTime.of(2023, 7, 25, 10, 0), recordDto.createdAt());
        assertEquals("admin", recordDto.createdBy());
        assertEquals(LocalDateTime.of(2023, 7, 25, 12, 0), recordDto.updatedAt());
        assertEquals("admin", recordDto.updatedBy());
    }
}

