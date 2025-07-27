package com.example.controller;

import com.example.BaseIntegrationTest;
import com.example.model.dto.RecordCreateRequest;
import com.example.model.dto.RecordUpdateRequest;
import com.example.model.entity.Record;
import com.example.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RecordControllerIntegrationTest extends BaseIntegrationTest {

    private TestRestTemplate restTemplate;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private RecordRepository recordRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port;
        restTemplate = new TestRestTemplate(restTemplateBuilder.rootUri(baseUrl).basicAuthentication("user", "user"));
        recordRepository.deleteAll();

        Record record = new Record();
        record.setUuid("123e4567-e89b-12d3-a456-426614174000");
        record.setName("Test Record");
        record.setStatus(Record.Status.ACTIVE);
        record.setCreatedAt(LocalDateTime.now());
        record.setCreatedBy("testUser");
        record.setUpdatedAt(LocalDateTime.now());
        record.setUpdatedBy("testUser");

        recordRepository.save(record);
    }

    @Test
    void shouldGetRecordById() {
        ResponseEntity<Record> response = restTemplate.getForEntity(
                "/records/123e4567-e89b-12d3-a456-426614174000", Record.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Record");
    }

    @Test
    void shouldGetAllRecords() {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "/records",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldCreateRecord() {
        RecordCreateRequest createRequest = new RecordCreateRequest("New Record");

        ResponseEntity<Record> response = restTemplate.postForEntity("/records", createRequest, Record.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("New Record");
    }

    @Test
    void shouldUpdateRecord() {
        RecordUpdateRequest updateRequest = new RecordUpdateRequest("Updated Record", Record.Status.INACTIVE);

        restTemplate.patchForObject(
                "/records/123e4567-e89b-12d3-a456-426614174000", updateRequest, Record.class);

        Record updatedRecord = recordRepository.findById("123e4567-e89b-12d3-a456-426614174000").orElseThrow();
        assertThat(updatedRecord.getName()).isEqualTo("Updated Record");
        assertThat(updatedRecord.getStatus()).isEqualTo(Record.Status.INACTIVE);
    }

    @Test
    void shouldDeleteRecord() {
        restTemplate.delete("/records/123e4567-e89b-12d3-a456-426614174000");

        boolean exists = recordRepository.existsById("123e4567-e89b-12d3-a456-426614174000");
        assertThat(exists).isFalse();
    }
}