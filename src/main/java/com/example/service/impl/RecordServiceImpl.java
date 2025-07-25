package com.example.service.impl;

import com.example.repository.RecordRepository;
import com.example.mapper.RecordMapper;
import com.example.model.dto.RecordCreateRequest;
import com.example.model.dto.RecordDto;
import com.example.model.dto.RecordUpdateRequest;
import com.example.model.entity.Record;
import com.example.service.RecordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;

    @Override
    public List<RecordDto> getAllRecords() {
        return recordRepository.findAll()
                .stream()
                .map(recordMapper::toDto)
                .toList();
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
            try {
                Record.Status statusEnum = Record.Status.valueOf(updateRequest.status());
                record.setStatus(statusEnum);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value: " + updateRequest.status());
            }
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

