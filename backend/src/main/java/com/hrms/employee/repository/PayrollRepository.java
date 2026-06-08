package com.hrms.employee.repository;

import com.hrms.employee.model.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollRepository extends JpaRepository<PayrollRecord,Integer> {
}
