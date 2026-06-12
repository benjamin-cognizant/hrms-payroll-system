package com.hrms.recruiter.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "JobRequisition")
public class JobRequisition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requisitionId;

    @NotNull(message = "Job title cannot be null")
    @NotEmpty(message = "Job title cannot be empty")
    @Size(min = 2, max = 100, message = "Job title must be between 2 and 100 characters")
    @Column(length = 100)
    private String jobTitle;

    @NotNull(message = "Department cannot be null")
    @NotEmpty(message = "Department cannot be empty")
    @Size(min = 2, max = 100, message = "Department must be between 2 and 100 characters")
    @Column(length = 100)
    private String department;

    @NotNull(message = "Number of positions cannot be null")
    @Min(value = 1, message = "Number of positions must be at least 1")
    @Max(value = 100, message = "Number of positions must not exceed 100")
    private Integer numberOfPositions;

    @NotNull(message = "Priority cannot be null")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    @Column(length = 2000, columnDefinition = "TEXT")
    private String description;

    private LocalDate requisitionDate;

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum Status {
        OPEN, IN_PROGRESS, FILLED, CLOSED
    }

    // Getters and Setters
    public Integer getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Integer requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getNumberOfPositions() {
        return numberOfPositions;
    }

    public void setNumberOfPositions(Integer numberOfPositions) {
        this.numberOfPositions = numberOfPositions;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(LocalDate requisitionDate) {
        this.requisitionDate = requisitionDate;
    }
}


