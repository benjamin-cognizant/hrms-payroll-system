package com.hrms.recruiter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AppraisalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appraisalId;

    public Integer getAppraisalId() { return appraisalId; }
    public void setAppraisalId(Integer appraisalId) { this.appraisalId = appraisalId; }
}
