package com.example.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "record")
@Getter
@Setter
public class Record {
    @Id
    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "status", nullable = false)
    private Status status;
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name="created_by", nullable = false)
    private String createdBy;
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name="updated_by", nullable = false)
    private String updatedBy;

    public enum Status {
       ACTIVE, INACTIVE, SUSPENDED
    }
}
