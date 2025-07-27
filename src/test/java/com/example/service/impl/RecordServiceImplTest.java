package com.example.service.impl;

import com.example.mapper.RecordMapper;
import com.example.model.dto.RecordCreateRequest;
import com.example.model.dto.RecordDto;
import com.example.model.dto.RecordFilter;
import com.example.model.dto.RecordUpdateRequest;
import com.example.model.entity.Record;
import com.example.repository.RecordRepository;
import com.example.repository.specification.RecordSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecordServiceImplTest {

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private RecordMapper recordMapper;

    @InjectMocks
    private RecordServiceImpl recordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAllRecords() {
        // Arrange
        RecordFilter filter = new RecordFilter();
        Pageable pageable = mock(Pageable.class);
        Record record = new Record();
        record.setUuid("123");
        Page<Record> recordPage = new PageImpl<>(List.of(record));
        when(recordRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(recordPage);
        when(recordMapper.toDto(any(Record.class))).thenReturn(new RecordDto("123", "Test", "ACTIVE", null, null, null, null));

        // Act
        Page<RecordDto> result = recordService.getAllRecords(pageable, filter);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("123", result.getContent().get(0).uuid());
        verify(recordRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testGetRecordByUuid() {
        // Arrange
        Record record = new Record();
        record.setUuid("123");
        when(recordRepository.findById("123")).thenReturn(Optional.of(record));
        when(recordMapper.toDto(record)).thenReturn(new RecordDto("123", "Test", "ACTIVE", null, null, null, null));

        // Act
        RecordDto result = recordService.getRecordByUuid("123");

        // Assert
        assertNotNull(result);
        assertEquals("123", result.uuid());
        verify(recordRepository, times(1)).findById("123");
    }

    @Test
    void testGetRecordByUuid_NotFound() {
        // Arrange
        when(recordRepository.findById("123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> recordService.getRecordByUuid("123"));
        verify(recordRepository, times(1)).findById("123");
    }

    @Test
    void testCreateRecord() {
        // Arrange
        RecordCreateRequest createRequest = new RecordCreateRequest("Test");
        Record record = new Record();
        record.setUuid(UUID.randomUUID().toString());
        when(recordRepository.save(any(Record.class))).thenReturn(record);
        when(recordMapper.toDto(any(Record.class))).thenReturn(new RecordDto(record.getUuid(), "Test", "ACTIVE", null, "testUser", null, "testUser"));

        // Act
        RecordDto result = recordService.createRecord(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test", result.name());
        assertEquals("testUser", result.createdBy());
        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    void testDeleteRecord() {
        // Arrange
        Record record = new Record();
        record.setUuid("123");
        when(recordRepository.findById("123")).thenReturn(Optional.of(record));

        // Act
        recordService.deleteRecord("123");

        // Assert
        verify(recordRepository, times(1)).delete(record);
    }

    @Test
    void testDeleteRecord_NotFound() {
        // Arrange
        when(recordRepository.findById("123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> recordService.deleteRecord("123"));
        verify(recordRepository, times(1)).findById("123");
    }

    @Test
    void testUpdateRecordDetails() {
        // Arrange
        RecordUpdateRequest updateRequest = new RecordUpdateRequest("Updated Name", Record.Status.INACTIVE);
        Record record = new Record();
        record.setUuid("123");
        record.setName("Old Name");
        record.setStatus(Record.Status.ACTIVE);
        when(recordRepository.findById("123")).thenReturn(Optional.of(record));
        when(recordRepository.save(any(Record.class))).thenReturn(record);
        when(recordMapper.toDto(any(Record.class))).thenReturn(new RecordDto("123", "Updated Name", "INACTIVE", null, null, null, "testUser"));

        // Act
        RecordDto result = recordService.updateRecordDetails("123", updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.name());
        assertEquals("INACTIVE", result.status());
        assertEquals("testUser", result.updatedBy());
        verify(recordRepository, times(1)).save(record);
    }
}

