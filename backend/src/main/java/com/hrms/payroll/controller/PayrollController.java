package com.hrms.payroll.controller;


//import com.hrms.payroll.model.Employee;
import com.hrms.payroll.model.Payroll;
import com.hrms.payroll.model.Status;
//import com.hrms.payroll.repository.EmployeeRepository;
import com.hrms.payroll.repository.PayrollRepository;
import com.hrms.payroll.service.PayrollService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.employeee.duplicate.model.Employee;
import com.employeee.duplicate.model.Payroll;
import com.employeee.duplicate.model.Status;
import com.employeee.duplicate.repository.EmployeeRepository;
import com.employeee.duplicate.repository.PayrollRepository;
import com.employeee.duplicate.service.PayrollService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
public class PayrollController {

    @Autowired
    private com.employeee.duplicate.service.PayrollService payrollService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PayrollRepository payrollRepository;

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("record", new Payroll());
        model.addAttribute("key", Status.values());
        // Pass all employees to populate the dropdown
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

        // 1. Calculate deductions
        double deductions = payrollService.deductions(payroll, payroll.getEmployee().getId());

        // 2. Extract the gross salary from the attached employee
        double grossSalary = payroll.getEmployee().getBaseSalary();

        // 3. Calculate net
        double netSalary = grossSalary - deductions;

        model.addAttribute("deduct", deductions);
        model.addAttribute("grossSalary", grossSalary);
        model.addAttribute("netSalary", netSalary);
        return "deduct";
    }


    @GetMapping("/payrolls")
    public String getPayrolls(@RequestParam(value = "month", required = false) String month, Model model) {
        if (month != null && !month.isEmpty()) {

            // 1. Convert "2026-01" directly to "JAN-2026" using Java Time
            String dbSearchMonth = YearMonth.parse(month)
                    .format(DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH))
                    .toUpperCase();

            // 2. Fetch data and set attributes
            model.addAttribute("payrolls", payrollService.getPayrollsByMonth(dbSearchMonth));
            model.addAttribute("selectedMonth", month);

        } else {

            // Fetch all data if no month is selected
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
            // This loads the actual payslip HTML page
            return "payslip";
        }
        return "redirect:/form";
    }

//    @GetMapping("/payslip/markAsPaid")
//    public String markAsPaid(@RequestParam("employeeId") Long employeeId) {
//        Payroll payroll = payrollService.getLatestPayrollByEmployeeId(employeeId);
//        if (payroll != null) {
//            payroll.setStatus(Status.PAID);
//            payrollService.savePayroll(payroll);
//            return "redirect:/payslip?employeeId=" + employeeId;
//        }
//        return "redirect:/form";
//    }


    @GetMapping("/payslip/markAsPaid")
    public String markAsPaid(@RequestParam("employeeId") Long employeeId) {
        Payroll payroll = payrollService.getLatestPayrollByEmployeeId(employeeId);
        if (payroll != null) {
            payroll.setStatus(Status.PAID);
            payrollService.savePayroll(payroll);

            // This redirect returns them to the table view where they clicked the button!
            return "redirect:/payrolls";
        }
        return "redirect:/form";
    }






    @GetMapping("/dashboard")
    public String getDashboard(@RequestParam(value = "month", required = false) String month, Model model) {


        if (month == null || month.isEmpty()) {
            month = "JAN-2026";
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
