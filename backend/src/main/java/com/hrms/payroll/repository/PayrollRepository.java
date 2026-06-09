package com.hrms.payroll.repository;

import com.hrms.payroll.model.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollRepository extends JpaRepository<PayrollRecord,Integer> {
}
