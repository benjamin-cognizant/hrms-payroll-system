package com.hrms.payroll.model;

import jakarta.persistence.*;

@Entity
public class PayrollRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payroll_id")
    private int payrollId;
    @Column(name="employee_id")
    private long employeeId;
    @Column(name="pay_period")
    private int payPeriod;
    @Column(name="gross_salary")
    private int grossSalary;
    @Column(name="total_deductions")
    private int totalDeductions;
    @Column(name="net_salary")
    private long netSalary;

    public int getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public int getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(int payPeriod) {
        this.payPeriod = payPeriod;
    }

    public int getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(int grossSalary) {
        this.grossSalary = grossSalary;
    }

    public int getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(int totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public long getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(long netSalary) {
        this.netSalary = netSalary;
    }
}

