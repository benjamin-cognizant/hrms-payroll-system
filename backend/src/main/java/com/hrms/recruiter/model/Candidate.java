package com.hrms.recruiter.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer candidateId;

    @Column(length = 100)
    private String fullName;

    @Column(length = 100)
    private String appliedRole;
    @Column(precision = 4, scale = 1)
    private BigDecimal experienceYears;

    @Column(length = 50)
    private String interviewStage;

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

    public BigDecimal getExperienceYears() { return experienceYears; }
    public void setExperienceYears(BigDecimal experienceYears) { this.experienceYears = experienceYears; }

    public String getInterviewStage() { return interviewStage; }
    public void setInterviewStage(String interviewStage) { this.interviewStage = interviewStage; }

    public CandidateStatus getCandidateStatus() { return candidateStatus; }
    public void setCandidateStatus(CandidateStatus candidateStatus) { this.candidateStatus = candidateStatus; }
}
