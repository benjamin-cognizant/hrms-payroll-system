package com.hrms.payroll.repository;

import com.hrms.payroll.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll,Integer> {

    Payroll findByEmployeeId(Long employeeId);

//    @Query("SELECT SUM(p.netSalary) FROM Payroll p")
//    Double calculateTotalNetPayroll();
//
//
//    @Query("SELECT AVG(p.netSalary) FROM Payroll p")
//    Double calculateAverageNetSalary();
}
