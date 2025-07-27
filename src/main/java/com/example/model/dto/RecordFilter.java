package com.example.model.dto;

import com.example.model.entity.Record;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecordFilter {
    private Record.Status status;
    private String name;
    private String createdBy;
}
