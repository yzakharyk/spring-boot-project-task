package com.example.repository;

import com.example.BaseIntegrationTest;
import com.example.model.dto.RecordFilter;
import com.example.model.entity.Record;
import com.example.model.entity.Record.Status;
import com.example.repository.specification.RecordSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RecordRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RecordRepository recordRepository;

    @BeforeEach
    void cleanDatabase() {
        recordRepository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        // Arrange
        var record = createRecord("123", "Test Record", Status.ACTIVE);

        // Act
        recordRepository.save(record);
        var retrievedRecord = recordRepository.findById("123");

        // Assert
        assertTrue(retrievedRecord.isPresent());
        assertEquals("Test Record", retrievedRecord.get().getName());
        assertEquals(Status.ACTIVE, retrievedRecord.get().getStatus());
    }

    @Test
    void testFindAll() {
        // Arrange
        var record1 = createRecord("123", "Active Record", Status.ACTIVE);
        var record2 = createRecord("456", "Inactive Record", Status.INACTIVE);

        recordRepository.save(record1);
        recordRepository.save(record2);

        // Act
        var records = recordRepository.findAll();

        // Assert
        assertEquals(2, records.size());
    }

    @Test
    void testFindAllWithFilters() {
        // Arrange
        var record1 = createRecord("123", "Active Record", Status.ACTIVE);
        var record2 = createRecord("456", "Inactive Record", Status.INACTIVE);
        recordRepository.save(record1);
        recordRepository.save(record2);

        var filter = new RecordFilter();
        filter.setStatus(Status.ACTIVE);

        // Act
        var specification = RecordSpecification.withFilters(filter);
        var filteredRecords = recordRepository.findAll(specification);

        // Assert
        assertEquals(1, filteredRecords.size());
        assertEquals("Active Record", filteredRecords.get(0).getName());
    }

    @Test
    void testFindAllWithPagination() {
        // Arrange
        IntStream.rangeClosed(1, 10).forEach(i -> {
            var record = createRecord(String.valueOf(i), "Record " + i, Status.ACTIVE);
            recordRepository.save(record);
        });

        var pageable = PageRequest.of(0, 5);

        // Act
        var page = recordRepository.findAll(pageable);

        // Assert
        assertEquals(5, page.getContent().size());
        assertEquals(10, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    void testSaveWithMissingFields() {
        // Arrange
        var record = new Record();
        record.setUuid("123");

        // Act & Assert
        assertThrows(Exception.class, () -> recordRepository.save(record));
    }

    @Test
    void testUpdateRecord() {
        // Arrange
        var record = createRecord("123", "Old Name", Status.ACTIVE);
        recordRepository.save(record);

        // Act
        record.setName("Updated Name");
        recordRepository.save(record);
        var updatedRecord = recordRepository.findById("123");

        // Assert
        assertTrue(updatedRecord.isPresent());
        assertEquals("Updated Name", updatedRecord.get().getName());
    }

    @Test
    void testDelete() {
        // Arrange
        var record = createRecord("123", "Test Record", Status.ACTIVE);
        recordRepository.save(record);

        // Act
        recordRepository.deleteById("123");
        var deletedRecord = recordRepository.findById("123");

        // Assert
        assertFalse(deletedRecord.isPresent());
    }

    private Record createRecord(String uuid, String name, Record.Status status) {
        var record = new Record();
        record.setUuid(uuid);
        record.setName(name);
        record.setStatus(status);
        record.setCreatedAt(LocalDateTime.now());
        record.setCreatedBy("testUser");
        record.setUpdatedAt(LocalDateTime.now());
        record.setUpdatedBy("testUser");
        return record;
    }
}