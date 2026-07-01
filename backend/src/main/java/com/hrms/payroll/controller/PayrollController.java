package com.hrms.payroll.controller;
import com.hrms.payroll.model.Payroll;
import com.hrms.payroll.model.Status;
import com.hrms.payroll.repository.EmployeeRepository;
import com.hrms.payroll.repository.PayrollRepository;
import com.hrms.payroll.service.PayrollService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import java.time.YearMonth;
import java.util.List;

@Controller
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PayrollRepository payrollRepository;

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("record", new Payroll());
        model.addAttribute("key", Status.values());
        model.addAttribute("employees", employeeRepository.findAll());
        return "submit";
    }

    @PostMapping("/run")
    public String runPayroll(@Valid @ModelAttribute("record") Payroll payroll, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("key", Status.values());
            model.addAttribute("employees", employeeRepository.findAll());
            return "submit";
        }

        try {

            payrollService.runPayroll(payroll, payroll.getEmployee().getId());
            return "redirect:/form";

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("key", Status.values());
            model.addAttribute("employees", employeeRepository.findAll());
            return "submit";
        }
    }

    @PostMapping("/deductions")
    public String computeStatutoryDeductions(@Valid @ModelAttribute("record") Payroll payroll, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("key", Status.values());
            model.addAttribute("employees", employeeRepository.findAll());
            return "submit";
        }


        double deductions = payrollService.deductions(payroll, payroll.getEmployee().getId());


        double grossSalary = payroll.getEmployee().getBaseSalary();

        double netSalary = grossSalary - deductions;

        model.addAttribute("deduct", deductions);
        model.addAttribute("grossSalary", grossSalary);
        model.addAttribute("netSalary", netSalary);
        return "deduct";
    }


    @GetMapping("/payrolls")
    public String getPayrolls(@RequestParam(value = "month", required = false) String month, Model model) {
        if (month != null && !month.isEmpty()) {

            model.addAttribute("payrolls", payrollService.getPayrollsByMonth(month));
            model.addAttribute("selectedMonth", month);

        } else {
            model.addAttribute("payrolls", payrollService.getData());
            model.addAttribute("selectedMonth", "");

        }

        return "record";
    }

    @GetMapping("/payslip")
    public String generatePayslip(@RequestParam("employeeId") Long employeeId, Model model) {
        Payroll payroll = payrollService.getLatestPayrollByEmployeeId(employeeId);
        if (payroll != null && payroll.getStatus() == Status.PAID) {
            model.addAttribute("payroll", payroll);
            return "payslip";
        }
        return "redirect:/form";
    }

    @GetMapping("/payslip/markAsPaid")
    public String markAsPaid(@RequestParam("employeeId") Long employeeId) {
        Payroll payroll = payrollService.getLatestPayrollByEmployeeId(employeeId);
        if (payroll != null) {
            payroll.setStatus(Status.PAID);
            payrollService.savePayroll(payroll);

            return "redirect:/payrolls";
        }
        return "redirect:/form";
    }

    @GetMapping("/dashboard")
    public String getDashboard(@RequestParam(value = "month", required = false) String month, Model model) {


        if (month == null || month.isEmpty()) {
            month = YearMonth.now().toString(); // e.g., "2026-06"
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

        model.addAttribute("selectedMonth", month);
        model.addAttribute("totalGross", totalGross);
        model.addAttribute("totalNetPay", totalNetPay);
        model.addAttribute("paidCount", paidCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("payrolls", monthlyPayrolls);

        return "dashboard";
    }
}