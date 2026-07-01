package com.hrms.employee.controller;

import com.hrms.employee.model.Payroll;
import com.hrms.employee.service.PayrollService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payroll")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    // 1. Run Payroll
    @PostMapping("/run/{id}")
    public ResponseEntity<Payroll> runPayroll(@PathVariable Long id, @RequestBody Payroll payrollDetails) {
        Payroll processed = payrollService.runPayroll(id, payrollDetails);
        return ResponseEntity.ok(processed);
    }

    // 5. Update Payroll Status
    @PutMapping("/{id}/status")
    public ResponseEntity<Payroll> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        Payroll updated = payrollService.updateStatus(id, newStatus);
        return ResponseEntity.ok(updated);
    }
    // 2. Generate Payslip
    @GetMapping("/{id}/payslip")
    public ResponseEntity<String> generatePayslip(@PathVariable Long id) {
        String payslip = payrollService.generatePayslip(id);
        return ResponseEntity.ok(payslip);
    }

    // 3. Compute Statutory Deductions
    @GetMapping("/{id}/deductions")
    public ResponseEntity<Double> computeStatutoryDeductions(@PathVariable Long id) {
        double deductions = payrollService.computeStatutoryDeductions(id);
        return ResponseEntity.ok(deductions);
    }

    // 4. File Compliance Report
    @PostMapping("/compliance")
    public ResponseEntity<String> fileComplianceReport(@RequestParam String period) {
        String report = payrollService.fileComplianceReport(period);
        return ResponseEntity.ok(report);
    }

    // Extra: View all payrolls
    @GetMapping("/view")
    public ResponseEntity<List<Payroll>> viewPayrolls() {
        return ResponseEntity.ok(payrollService.getAllPayrolls());
    }

    // Get total gross processed
    @GetMapping("/gross")
    public ResponseEntity<Double> getTotalGrossProcessed() {
        double totalGross = payrollService.getTotalGrossProcessed();
        return ResponseEntity.ok(totalGross);
    }

    @GetMapping("/deductions")
    public ResponseEntity<Double> getTotalDeductions() {
        double total = payrollService.getTotalDeductions();
        return ResponseEntity.ok(total);
    }

    // Get total net payout
    @GetMapping("/net")
    public ResponseEntity<Double> getTotalNetPayout() {
        double totalNet = payrollService.getTotalNetPayout();
        return ResponseEntity.ok(totalNet);
    }

    // Get average salary
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageSalary() {
        double avgSalary = payrollService.getAverageSalary();
        return ResponseEntity.ok(avgSalary);
    }
}