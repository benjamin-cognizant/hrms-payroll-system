package com.hrms.employee.repository;

import com.hrms.employee.model.AppraisalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppraisalRepository extends JpaRepository<AppraisalRecord,Integer> {
}
