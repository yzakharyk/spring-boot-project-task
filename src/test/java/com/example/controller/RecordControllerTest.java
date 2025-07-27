package com.example.controller;

import com.example.model.dto.RecordCreateRequest;
import com.example.model.dto.RecordDto;
import com.example.model.dto.RecordFilter;
import com.example.model.dto.RecordUpdateRequest;
import com.example.model.entity.Record;
import com.example.service.RecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class RecordControllerTest {

    @Mock
    private RecordService recordService;

    @InjectMocks
    private RecordController recordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRecords() {
        // Arrange
        var pageable = PageRequest.of(0, 10);
        var filter = new RecordFilter();
        var recordDto = new RecordDto("123", "Test Record", "ACTIVE", null, null, null, null);
        var recordPage = new PageImpl<>(List.of(recordDto));
        when(recordService.getAllRecords(any(Pageable.class), any(RecordFilter.class))).thenReturn(recordPage);

        // Act
        var result = recordController.getAllRecords(0, 10, "createdAt", "desc", filter);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("123", result.getContent().get(0).uuid());
        verify(recordService, times(1)).getAllRecords(any(Pageable.class), any(RecordFilter.class));
    }

    @Test
    void testGetRecordByUuid() {
        // Arrange
        var uuid = UUID.randomUUID().toString();
        var recordDto = new RecordDto(uuid, "Test Record", "ACTIVE", null, null, null, null);
        when(recordService.getRecordByUuid(uuid)).thenReturn(recordDto);

        // Act
        var result = recordController.getRecordByUuid(uuid);

        // Assert
        assertNotNull(result);
        assertEquals(uuid, result.uuid());
        verify(recordService, times(1)).getRecordByUuid(uuid);
    }

    @Test
    void testCreateRecord() {
        // Arrange
        var createRequest = new RecordCreateRequest("New Record");
        var recordDto = new RecordDto("123", "New Record", "ACTIVE", null, null, null, null);
        when(recordService.createRecord(createRequest)).thenReturn(recordDto);

        // Act
        var result = recordController.createRecord(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("New Record", result.name());
        verify(recordService, times(1)).createRecord(createRequest);
    }

    @Test
    void testDeleteRecord() {
        // Arrange
        var uuid = UUID.randomUUID().toString();
        doNothing().when(recordService).deleteRecord(uuid);

        // Act
        recordController.deleteRecord(uuid);

        // Assert
        verify(recordService, times(1)).deleteRecord(uuid);
    }

    @Test
    void testUpdateRecordDetails() {
        // Arrange
        var uuid = UUID.randomUUID().toString();
        var updateRequest = new RecordUpdateRequest("Updated Record", Record.Status.INACTIVE);
        var recordDto = new RecordDto(uuid, "Updated Record", "INACTIVE", null, null, null, null);
        when(recordService.updateRecordDetails(uuid, updateRequest)).thenReturn(recordDto);

        // Act
        var result = recordController.updateRecordDetails(uuid, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Record", result.name());
        assertEquals("INACTIVE", result.status());
        verify(recordService, times(1)).updateRecordDetails(uuid, updateRequest);
    }

    @Test
    void testValidateSortProperty_InvalidProperty() {
        // Arrange
        var invalidSortBy = "invalidProperty";

        // Act & Assert
        var exception = assertThrows(ResponseStatusException.class, () ->
            recordController.getAllRecords(0, 10, invalidSortBy, "asc", new RecordFilter())
        );
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Invalid sort property"));
    }
}
