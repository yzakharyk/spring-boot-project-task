package com.example.service;

import com.example.model.dto.RecordCreateRequest;
import com.example.model.dto.RecordDto;
import com.example.model.dto.RecordUpdateRequest;

import java.util.List;

public interface RecordService {
    List<RecordDto> getAllRecords();
    RecordDto getRecordByUuid(String uuid);

    RecordDto createRecord(RecordCreateRequest createRequest);

    void deleteRecord(String uuid);

    RecordDto updateRecordDetails(String uuid, RecordUpdateRequest updateRequest);
}
