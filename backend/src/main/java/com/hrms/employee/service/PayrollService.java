package com.hrms.employee.service;


import com.hrms.employee.model.Payroll;
import com.hrms.employee.repository.PayrollRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;

    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    public double getTotalGrossProcessed() {
        return payrollRepository.findAll()
                .stream()
                .mapToDouble(Payroll::getSalary)
                .sum();
    }
    // Run Payroll
    public Payroll runPayroll(Long id, Payroll payrollDetails) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        // Update payment-related fields
        payroll.setSalary(payrollDetails.getSalary());
        payroll.setPayPeriod(payrollDetails.getPayPeriod());

        // Compute deductions and net salary
        double deductions = payroll.getSalary() * 0.20;
        payroll.setDeductions(deductions);
        payroll.setNetSalary(payroll.getSalary() - deductions);

        // Mark as PAID
        payroll.setStatus("PAID");

        return payrollRepository.save(payroll);
    }

    // Generate Payslip
    public String generatePayslip(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));
        return "Payslip for Employee " + payroll.getEmployeeName() +
                " | Period: " + payroll.getPayPeriod() +
                " | Net Salary: " + payroll.getNetSalary();
    }

    // Compute Statutory Deductions
    public double computeStatutoryDeductions(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));
        return payroll.getDeductions();
    }

    // File Compliance Report
    public String fileComplianceReport(String period) {
        return "Compliance report for " + period + " filed successfully.";
    }

    // View all payrolls
    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    public double getTotalDeductions() {
        return payrollRepository.findAll()
                .stream()
                .mapToDouble(Payroll::getDeductions)
                .sum();
    }

    public Payroll updateStatus(Long id, String newStatus) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));
        payroll.setStatus(newStatus);
        return payrollRepository.save(payroll);
    }
    public double getTotalNetPayout() {
        return payrollRepository.findAll()
                .stream()
                .mapToDouble(Payroll::getNetSalary)
                .sum();
    }

    public double getAverageSalary() {
        return payrollRepository.findAll()
                .stream()
                .mapToDouble(Payroll::getSalary)
                .average()
                .orElse(0.0);
    }
}
