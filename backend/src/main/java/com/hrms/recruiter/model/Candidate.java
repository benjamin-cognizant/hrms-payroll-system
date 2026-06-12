package com.hrms.recruiter.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer candidateId;

    @NotNull(message = "Full name cannot be null")
    @NotEmpty(message = "Full name cannot be empty")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Column(length = 100)
    private String fullName;

    @NotNull(message = "Applied role cannot be null")
    @NotEmpty(message = "Applied role cannot be empty")
    @Size(min = 2, max = 100, message = "Applied role must be between 2 and 100 characters")
    @Column(length = 100)
    private String appliedRole;


    @NotNull(message = "Experience years cannot be null")
    @Min(value = 0, message = "Experience years must be at least 0")
    @Max(value = 50, message = "Experience years must not exceed 50")
    private Integer experienceYears;

    @NotNull(message = "Interview stage cannot be null")
    @NotEmpty(message = "Interview stage cannot be empty")
    @Column(length = 50)
    private String interviewStage;

    @NotNull(message = "Candidate status cannot be null")
    @Enumerated(EnumType.STRING)
    private CandidateStatus candidateStatus;
    public enum CandidateStatus {
        APPLIED, IN_INTERVIEW, OFFERED, HIRED, REJECTED
    }
    public Integer getCandidateId() { return candidateId; }
    public void setCandidateId(Integer candidateId) { this.candidateId = candidateId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAppliedRole() { return appliedRole; }
    public void setAppliedRole(String appliedRole) { this.appliedRole = appliedRole; }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getInterviewStage() { return interviewStage; }
    public void setInterviewStage(String interviewStage) { this.interviewStage = interviewStage; }

    public CandidateStatus getCandidateStatus() { return candidateStatus; }
    public void setCandidateStatus(CandidateStatus candidateStatus) { this.candidateStatus = candidateStatus; }
}
