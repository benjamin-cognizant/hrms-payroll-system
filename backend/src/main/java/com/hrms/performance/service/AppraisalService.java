package com.hrms.performance.service;

import com.hrms.performance.model.AppraisalRecord;
import com.hrms.performance.repository.AppraisalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class AppraisalService {

    @Autowired
    private AppraisalRepository appraisalRepository;

    public AppraisalRecord initializeGoals(Long employeeId, String cycle) {
        AppraisalRecord record = appraisalRepository.findByEmployeeIdAndAppraisalCycle(employeeId, cycle)
                .orElse(new AppraisalRecord());
        
        record.setEmployeeId(employeeId);
        record.setAppraisalCycle(cycle);
        record.setAppraisalStatus(AppraisalRecord.AppraisalStatus.DRAFT);
        return appraisalRepository.save(record);
    }

    public AppraisalRecord submitSelfReview(Long appraisalId, Integer goalsAchieved) {
        AppraisalRecord record = appraisalRepository.findById(appraisalId)
                .orElseThrow(() -> new RuntimeException("Appraisal record not found"));
        
        record.setGoalsAchieved(goalsAchieved);
        record.setAppraisalStatus(AppraisalRecord.AppraisalStatus.SELF_REVIEW);
        return appraisalRepository.save(record);
    }

    public AppraisalRecord submitManagerReview(Long appraisalId, BigDecimal rating) {
        AppraisalRecord record = appraisalRepository.findById(appraisalId)
                .orElseThrow(() -> new RuntimeException("Appraisal record not found"));
        
        record.setOverallRating(rating);
        record.setAppraisalStatus(AppraisalRecord.AppraisalStatus.MANAGER_REVIEW);
        return appraisalRepository.save(record);
    }

    public AppraisalRecord publishFinalRating(Long appraisalId) {
        AppraisalRecord record = appraisalRepository.findById(appraisalId)
                .orElseThrow(() -> new RuntimeException("Appraisal record not found"));
        
        record.setAppraisalStatus(AppraisalRecord.AppraisalStatus.PUBLISHED);
        return appraisalRepository.save(record);
    }
}
