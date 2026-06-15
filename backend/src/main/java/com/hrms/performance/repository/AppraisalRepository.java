package com.hrms.performance.repository;

import com.hrms.performance.model.AppraisalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AppraisalRepository extends JpaRepository<AppraisalRecord, Long> {
    Optional<AppraisalRecord> findByEmployeeIdAndAppraisalCycle(Long employeeId, String appraisalCycle);
}    