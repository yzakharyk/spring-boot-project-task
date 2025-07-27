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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;

    @Override
    public Page<RecordDto> getAllRecords(Pageable pageable, RecordFilter recordFilter) {
        var specification = RecordSpecification.withFilters(recordFilter);
        Page<Record> recordPage = recordRepository.findAll(specification, pageable);
        return recordPage.map(recordMapper::toDto);
    }

    @Override
    public RecordDto getRecordByUuid(String uuid) {
        return recordMapper.toDto(findRecordByUuid(uuid));
    }

    @Override
    public RecordDto createRecord(RecordCreateRequest createRequest) {
        var record = new Record();
        record.setUuid(UUID.randomUUID().toString());
        record.setName(createRequest.name());
        record.setStatus(Record.Status.ACTIVE);
        record.setCreatedAt(LocalDateTime.now());
        record.setCreatedBy("admin");
        record.setUpdatedAt(LocalDateTime.now());
        record.setUpdatedBy("admin");
        var createdRecord = recordRepository.save(record);
        return recordMapper.toDto(createdRecord);
    }

    @Override
    public void deleteRecord(String uuid) {
        var record = findRecordByUuid(uuid);
        recordRepository.delete(record);
    }

    @Override
    public RecordDto updateRecordDetails(String uuid, RecordUpdateRequest updateRequest) {
        var record = findRecordByUuid(uuid);

        if (updateRequest.name() != null) {
            record.setName(updateRequest.name());
        }
        if (updateRequest.status() != null) {
            record.setStatus(updateRequest.status());
        }

        record.setUpdatedAt(LocalDateTime.now());
        record.setUpdatedBy("admin");

        var updatedRecord = recordRepository.save(record);
        return recordMapper.toDto(updatedRecord);
    }

    private Record findRecordByUuid(String uuid) {
        return recordRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Record not found with uuid: " + uuid));
    }
}

