package com.example.controller;

import com.example.model.dto.RecordCreateRequest;
import com.example.model.dto.RecordDto;
import com.example.model.dto.RecordFilter;
import com.example.model.dto.RecordUpdateRequest;
import com.example.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
@Tag(name = "Record Management", description = "APIs for managing records")
public class RecordController {
    public static final int MAX_PAGE_SIZE = 100;
    private final RecordService recordService;

    @GetMapping
    @Operation(summary = "Get all records", description = "Retrieve a paginated list of all records")
    public Page<RecordDto> getAllRecords(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number, starting from 0") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of records per page") int size,
            @RequestParam(defaultValue = "createdAt") @Parameter(description = "Field to sort by") String sortBy,
            @RequestParam(defaultValue = "desc") @Parameter(description = "Sort direction (asc or desc)") String sortDir,
            @Parameter(description = "Filter criteria for records", allowEmptyValue = true) RecordFilter recordFilter) {

        validateSortProperty(sortBy);
        int pageSize = Math.min(size, MAX_PAGE_SIZE);
        var pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        return recordService.getAllRecords(pageable, recordFilter);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get record by UUID", description = "Retrieve a specific record by its UUID")
    public RecordDto getRecordByUuid(
            @PathVariable @Parameter(description = "UUID of the record to retrieve") String uuid) {
        return recordService.getRecordByUuid(uuid);
    }

    @PostMapping
    @Operation(summary = "Create a new record", description = "Create a new record with the provided details")
    public RecordDto createRecord(
            @RequestBody @Parameter(description = "Details of the record to create") RecordCreateRequest createRequest) {
        return recordService.createRecord(createRequest);
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete a record", description = "Delete a specific record by its UUID")
    public void deleteRecord(
            @PathVariable @Parameter(description = "UUID of the record to delete") String uuid) {
        recordService.deleteRecord(uuid);
    }

    @PatchMapping("/{uuid}")
    @Operation(summary = "Update record details", description = "Update the details of a specific record by its UUID")
    public RecordDto updateRecordDetails(
            @PathVariable @Parameter(description = "UUID of the record to update") String uuid,
            @RequestBody @Parameter(description = "Updated details of the record") RecordUpdateRequest updateRequest) {
        return recordService.updateRecordDetails(uuid, updateRequest);
    }

    private void validateSortProperty(String sortBy) {
        var validProperties = Set.of("uuid", "name", "status", "createdAt", "createdBy", "updatedAt", "updatedBy");

        if (!validProperties.contains(sortBy)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sort property: " + sortBy + ". Valid properties are: " + validProperties
            );
        }
    }
}
