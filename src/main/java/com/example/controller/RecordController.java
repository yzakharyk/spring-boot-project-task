package com.example.controller;

import com.example.model.dto.RecordCreateRequest;
import com.example.model.dto.RecordDto;
import com.example.model.dto.RecordUpdateRequest;
import com.example.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @GetMapping
    public List<RecordDto> getAllRecords() {
        return recordService.getAllRecords();
    }

    @GetMapping("/{uuid}")
    public RecordDto getRecordByUuid(@PathVariable String uuid) {
        return recordService.getRecordByUuid(uuid);
    }

    @PostMapping
    public RecordDto createRecord(@RequestBody RecordCreateRequest createRequest) {
        return recordService.createRecord(createRequest);
    }

    @DeleteMapping("/{uuid}")
    public void deleteRecord(@PathVariable String uuid) {
         recordService.deleteRecord(uuid);
    }

    @PatchMapping("/{uuid}")
    public RecordDto updateRecordDetails(@PathVariable String uuid, @RequestBody RecordUpdateRequest updateRequest) {
        return recordService.updateRecordDetails(uuid, updateRequest);
    }
}
