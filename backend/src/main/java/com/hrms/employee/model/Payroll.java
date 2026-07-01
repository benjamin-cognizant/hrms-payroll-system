package com.hrms.employee.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    private Long employeeId;
    private String employeeName;
    private double salary;
    private String payPeriod;
    private double deductions;
    private double netSalary;
    private String status; // PAID, ON_HOLD, etc.

    // getters and setters
}
