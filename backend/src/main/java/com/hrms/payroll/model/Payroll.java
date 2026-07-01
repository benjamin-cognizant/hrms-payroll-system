package com.hrms.payroll.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payroll_id")
    private int payrollId;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;
    public Payroll() {

    }
    public Payroll(int payrollId, Employee employee, String employeeId, String payPeriod, double grossSalary, double totalDeductions, double netSalary, Status status) {
        this.payrollId = payrollId;
        this.employee = employee;
        this.employeeId = employeeId;
        this.payPeriod = payPeriod;
        this.grossSalary = grossSalary;
        this.totalDeductions = totalDeductions;
        this.netSalary = netSalary;
        this.status = status;
    }

    @NotNull(message = "Employee ID cannot be null")
    @Column(name="employee_id")
    @Size(min = 7, message = "Employee ID must be a positive number")
    private String employeeId;

    @NotBlank(message = "Pay period cannot be empty")
    @Size(max = 20, message = "Pay period must not exceed 20 characters")
    @Column(name="pay_period")
    private String payPeriod;

    @NotNull(message = "Gross salary is required")
    @PositiveOrZero(message = "Gross salary cannot be zero and negative")
    @Column(name="gross_salary")
    private double grossSalary;

//    @NotNull(message = "Total deductions is required")
    @PositiveOrZero(message = "Total deductions cannot be negative")
    @Column(name="total_deductions")
    private double totalDeductions;

//    @NotNull(message = "Net salary is required")
    @PositiveOrZero(message = "Net salary cannot be negative")
    @Column(name="net_salary")
    private double netSalary;

    @NotNull(message = "Status cannot be empty")
    private Status status;



//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//
//        this.status = status;
//    }
//
//    public int getPayrollId() {
//
//        return payrollId;
//    }
//
//    public void setPayrollId(int payrollId) {
//
//        this.payrollId = payrollId;
//    }
//
//    public String getEmployeeId() {
//
//        return employeeId;
//    }
//
//    public void setEmployeeId(String employeeId) {
//
//        this.employeeId = employeeId;
//    }
//
//    public String getPayPeriod() {
//
//        return payPeriod;
//    }
//
//    public void setPayPeriod(String payPeriod)
//    {
//        this.payPeriod = payPeriod;
//    }
//
//    public double getGrossSalary() {
//        return grossSalary;
//    }
//
//    public void setGrossSalary(double grossSalary) {
//        this.grossSalary = grossSalary;
//    }
//
//    public double getTotalDeductions() {
//        return totalDeductions;
//    }
//
//    public void setTotalDeductions(double totalDeductions) {
//        this.totalDeductions = totalDeductions;
//    }
//
//    public double getNetSalary() {
//        return netSalary;
//    }
//
//    public void setNetSalary(double netSalary) {
//        this.netSalary = netSalary;
//    }
}

