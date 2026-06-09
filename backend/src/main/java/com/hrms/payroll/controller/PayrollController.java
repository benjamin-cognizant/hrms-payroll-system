package com.hrms.payroll.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PayrollController {

    @PostMapping("/payroll")
    public String runPayroll(){
        return "success";
    }

    @GetMapping("/payslip")
    public String generatePayslip(){
        return "downloaded";
    }

    @PostMapping("/tax")
    public String computeStatutoryDeductions(){
        return "taxes";
    }

    @PostMapping("/report")
    public String fileComplianceReport(){
        return "report";
    }



}
