package com.hrms.recruiter.repository;

import com.hrms.recruiter.model.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollRepository extends JpaRepository<PayrollRecord,Integer> {
}
