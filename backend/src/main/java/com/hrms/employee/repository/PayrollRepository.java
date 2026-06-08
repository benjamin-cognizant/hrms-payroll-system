package com.cognizant.hrms.repository;

import com.cognizant.hrms.model.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollRepository extends JpaRepository<PayrollRecord,Integer> {
}
