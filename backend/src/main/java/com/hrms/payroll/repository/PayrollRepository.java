package com.hrms.payroll.repository;

import com.hrms.payroll.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll,Integer> {

    Payroll findTopByEmployeeIdOrderByPayrollIdDesc(Long employeeId);

    List<Payroll> findByPayPeriod(String payPeriod);


    boolean existsByEmployee_IdAndPayPeriod(Long employeeId, String payPeriod);

}
