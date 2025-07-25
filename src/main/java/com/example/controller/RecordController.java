package com.example.controller;

import com.example.model.dto.RecordCreateRequest;
import com.example.model.dto.RecordDto;
import com.example.model.dto.RecordUpdateRequest;
import com.example.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {
    public static final int MAX_PAGE_SIZE = 100;
    private final RecordService recordService;

   @GetMapping
   public Page<RecordDto> getAllRecords(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int size,
           @RequestParam(defaultValue = "createdAt") String sortBy,
           @RequestParam(defaultValue = "desc") String sortDir) {

       validateSortProperty(sortBy);
       int pageSize = Math.min(size, MAX_PAGE_SIZE);
       var pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
       return recordService.getAllRecords(pageable);
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
