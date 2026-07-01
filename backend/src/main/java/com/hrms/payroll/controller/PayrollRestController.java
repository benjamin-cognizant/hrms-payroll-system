package com.hrms.payroll.controller;

import com.hrms.payroll.model.Employee;
import com.hrms.payroll.model.Payroll;
import com.hrms.payroll.model.Status;
import com.hrms.payroll.repository.EmployeeRepository;
import com.hrms.payroll.repository.PayrollRepository;
import com.hrms.payroll.service.PayrollService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payroll")
@CrossOrigin(origins = "*")
public class PayrollRestController {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PayrollRepository payrollRepository;

    // 1. Provide data needed to populate the frontend form
    @GetMapping("/form-data")
    public ResponseEntity<Map<String, Object>> getFormData() {
        Map<String, Object> response = new HashMap<>();
        response.put("statuses", Status.values());
        response.put("employees", employeeRepository.findAll());
        return ResponseEntity.ok(response);
    }


    // 2. Run Payroll
    @PostMapping("/run")
    public ResponseEntity<?> runPayroll(@Valid @RequestBody Payroll payroll) {
        try {
            // 1. Fetch the real employee from the DB to ensure they exist and we have their real salary
            Employee realEmployee = employeeRepository.findById(payroll.getEmployee().getId())
                    .orElseThrow(() -> new IllegalStateException("Employee not found"));

            // 2. Attach the real employee to the payroll object
            payroll.setEmployee(realEmployee);

            // 3. Set the math fields before saving
            double deductions = payrollService.deductions(payroll, realEmployee.getId());
            payroll.setGrossSalary(realEmployee.getBaseSalary());
            payroll.setTotalDeductions(deductions);
            payroll.setNetSalary(realEmployee.getBaseSalary() - deductions);

            // 4. Run the service
            payrollService.runPayroll(payroll, realEmployee.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Payroll executed successfully"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // 3. Compute Deductions and return the math
    @PostMapping("/deductions")
    public ResponseEntity<Map<String, Double>> computeStatutoryDeductions(@RequestBody Payroll payroll) {

        // Fetch the real employee from the DB
        Employee realEmployee = employeeRepository.findById(payroll.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        double deductions = payrollService.deductions(payroll, realEmployee.getId());
        double grossSalary = realEmployee.getBaseSalary();
        double netSalary = grossSalary - deductions;

        Map<String, Double> response = new HashMap<>();
        response.put("deduct", deductions);
        response.put("grossSalary", grossSalary);
        response.put("netSalary", netSalary);

        return ResponseEntity.ok(response);
    }

    // 4. Get Payrolls list
    @GetMapping("/payrolls")
    public ResponseEntity<List<Payroll>> getPayrolls(@RequestParam(value = "month", required = false) String month) {
        List<Payroll> payrolls;
        if (month != null && !month.isEmpty()) {
            payrolls = payrollService.getPayrollsByMonth(month);
        } else {
            payrolls = payrollService.getData();
        }
        return ResponseEntity.ok(payrolls);
    }

    // 5. Get a specific payslip
    @GetMapping("/payslip")
    public ResponseEntity<Payroll> generatePayslip(@RequestParam("employeeId") Long employeeId) {
        Payroll payroll = payrollService.getLatestPayrollByEmployeeId(employeeId);
        if (payroll != null && payroll.getStatus() == Status.PAID) {
            return ResponseEntity.ok(payroll);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 6. Update status to PAID
    @PutMapping("/payslip/markAsPaid/{employeeId}")
    public ResponseEntity<?> markAsPaid(@PathVariable("employeeId") Long employeeId) {
        Payroll payroll = payrollService.getLatestPayrollByEmployeeId(employeeId);
        if (payroll != null) {
            payroll.setStatus(Status.PAID);
            payrollService.savePayroll(payroll);
            return ResponseEntity.ok(Map.of("message", "Status updated to PAID"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Payroll not found"));
    }

    // 7. Get Dashboard Data
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(@RequestParam(value = "month", required = false) String month) {
        if (month == null || month.isEmpty()) {
            month = YearMonth.now().toString();
        }

        List<Payroll> monthlyPayrolls = payrollRepository.findByPayPeriod(month);

        double totalGross = 0;
        double totalNetPay = 0;
        int paidCount = 0;
        int pendingCount = 0;

        for (Payroll p : monthlyPayrolls) {
            totalGross += p.getGrossSalary();
            totalNetPay += p.getNetSalary();
            if (p.getStatus() != null && "PAID".equals(p.getStatus().name())) {
                paidCount++;
            } else {
                pendingCount++;
            }
        }

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("selectedMonth", month);
        dashboardData.put("totalGross", totalGross);
        dashboardData.put("totalNetPay", totalNetPay);
        dashboardData.put("paidCount", paidCount);
        dashboardData.put("pendingCount", pendingCount);
        dashboardData.put("payrolls", monthlyPayrolls);

        return ResponseEntity.ok(dashboardData);
    }
}