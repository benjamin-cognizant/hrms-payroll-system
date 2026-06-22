package com.hrms.employee.controller;

import com.hrms.employee.model.Employee;
import com.hrms.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("Loading employee dashboard");
        model.addAttribute("employee", new Employee());
        return "dashboard";
    }

    @PostMapping("/createEmployee")
    public String createEmployee(@Valid @ModelAttribute Employee employee, Model model) {
        log.info("Received request to create employee: {}", employee.getName());
        Employee created = employeeService.createEmployee(employee);
        log.info("Employee created successfully with ID:{} ", created.getId());
        model.addAttribute("employee", created);
        return "create-employee";
    }

    @GetMapping("/{id}")
    public String getEmployeeDetails(@PathVariable Integer id, Model model) {
        log.debug("Fetching details for employee ID: {}", id);
        Employee employee = employeeService.getEmployeeById(id);
        log.info("Employee details retrieved");
        model.addAttribute("employee", employee);
        return "employee-details";
    }
}