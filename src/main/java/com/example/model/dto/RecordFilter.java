package com.example.model.dto;

import com.example.model.entity.Record;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordFilter {
    private Record.Status status;
    private String name;
    private String createdBy;
}
