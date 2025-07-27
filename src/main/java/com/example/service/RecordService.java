package com.example.service;

import com.example.model.dto.RecordCreateRequest;
import com.example.model.dto.RecordDto;
import com.example.model.dto.RecordFilter;
import com.example.model.dto.RecordUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecordService {
    Page<RecordDto> getAllRecords(Pageable pageable, RecordFilter recordFilter);

    RecordDto getRecordByUuid(String uuid);

    RecordDto createRecord(RecordCreateRequest createRequest);

    void deleteRecord(String uuid);

    RecordDto updateRecordDetails(String uuid, RecordUpdateRequest updateRequest);
}
