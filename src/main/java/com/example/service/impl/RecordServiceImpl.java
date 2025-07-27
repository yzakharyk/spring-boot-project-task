package com.example.service.impl;

import com.example.model.dto.RecordFilter;
import com.example.repository.RecordRepository;
import com.example.mapper.RecordMapper;
import com.example.model.dto.RecordCreateRequest;
import com.example.model.dto.RecordDto;
import com.example.model.dto.RecordUpdateRequest;
import com.example.model.entity.Record;
import com.example.repository.specification.RecordSpecification;
import com.example.service.RecordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;

    @Override
    public Page<RecordDto> getAllRecords(Pageable pageable, RecordFilter recordFilter) {
        log.info("Fetching all records with filter: {}", recordFilter);
        var specification = RecordSpecification.withFilters(recordFilter);
        Page<Record> recordPage = recordRepository.findAll(specification, pageable);
        return recordPage.map(recordMapper::toDto);
    }

    @Override
    public RecordDto getRecordByUuid(String uuid) {
        log.info("Fetching record with UUID: {}", uuid);
        return recordMapper.toDto(findRecordByUuid(uuid));
    }

    @Override
    public RecordDto createRecord(RecordCreateRequest createRequest) {
        log.info("Creating a new record with name: {}", createRequest.name());
        var record = new Record();
        record.setUuid(UUID.randomUUID().toString());
        record.setName(createRequest.name());
        record.setStatus(Record.Status.ACTIVE);
        record.setCreatedAt(LocalDateTime.now());
        record.setCreatedBy(getCurrentUsername());
        record.setUpdatedAt(LocalDateTime.now());
        record.setUpdatedBy(getCurrentUsername());
        var createdRecord = recordRepository.save(record);
        return recordMapper.toDto(createdRecord);
    }

    @Override
    public void deleteRecord(String uuid) {
        log.info("Deleting record with UUID: {}", uuid);
        var record = findRecordByUuid(uuid);
        recordRepository.delete(record);
    }

    @Override
    public RecordDto updateRecordDetails(String uuid, RecordUpdateRequest updateRequest) {
        log.info("Updating record with UUID: {}", uuid);
        var record = findRecordByUuid(uuid);

        if (updateRequest.name() != null) {
            record.setName(updateRequest.name());
        }
        if (updateRequest.status() != null) {
            record.setStatus(updateRequest.status());
        }

        record.setUpdatedAt(LocalDateTime.now());
        record.setUpdatedBy(getCurrentUsername());

        var updatedRecord = recordRepository.save(record);
        return recordMapper.toDto(updatedRecord);
    }

    private Record findRecordByUuid(String uuid) {
        return recordRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Record not found with uuid: " + uuid));
    }

    private String getCurrentUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Principal::getName)
                .orElseThrow(() -> new IllegalStateException("No authentication found"));
    }
}

