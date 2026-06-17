package com.hrms.employee.controller;

import com.hrms.employee.model.Employee;
import com.hrms.employee.service.EmployeeService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/createEmployee")
    public String createEmployee(@Valid @ModelAttribute Employee employee, Model model) {
        Employee created = employeeService.createEmployee(employee);
        model.addAttribute("employee", created);
        return "create-employee"; // confirmation page
    }

    @GetMapping("/{id}")
    public String getEmployeeDetails(@PathVariable Integer id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "employee-details";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Provide an empty Employee object for the form
        model.addAttribute("employee", new Employee());
        return "employee"; // points to dashboard.html
    }
}
