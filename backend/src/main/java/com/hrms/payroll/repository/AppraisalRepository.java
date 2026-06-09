package com.hrms.payroll.repository;

import com.hrms.payroll.model.AppraisalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppraisalRepository extends JpaRepository<AppraisalRecord,Integer> {
}
