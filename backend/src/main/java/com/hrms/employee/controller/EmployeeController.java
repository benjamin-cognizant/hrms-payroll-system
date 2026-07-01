package com.hrms.employee.controller;


import com.hrms.employee.model.Employee;
import com.hrms.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Get all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeService.getAllEmployees();
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public Employee getEmployeeDetails(@PathVariable Integer id) {
        log.debug("Fetching details for employee ID: {}", id);
        return employeeService.getEmployeeById(id);
    }

    // Create new employee
    @PostMapping
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        log.info("Received request to create employee: {}", employee.getName());
        Employee created = employeeService.createEmployee(employee);
        log.info("Employee created successfully with ID: {}", created.getId());
        return created;
    }

    // Delete employee
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Integer id) {
        log.info("Deleting employee with ID: {}", id);
        employeeService.deleteEmployee(id);
    }

    // Update employee details
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Integer id,
                                   @Valid @RequestBody Employee employee) {
        log.info("Updating employee with ID: {}", id);
        Employee updated = employeeService.updateEmployee(id, employee);
        log.info("Employee updated successfully with ID: {}", updated.getId());
        return updated;
    }

    // Assign a manager to an employee
    @PutMapping("/{id}/manager/{managerId}")
    public Employee assignManager(@PathVariable Integer id,
                                  @PathVariable Integer managerId) {
        log.info("Assigning manager with ID {} to employee ID {}", managerId, id);
        Employee updated = employeeService.assignManager(id, managerId);
        log.info("Manager assigned successfully to employee ID {}", id);
        return updated;
    }

    // Individual field endpoints
    @GetMapping("/{id}/getId")
    public Integer getId(@PathVariable Integer id){
        return employeeService.getId(id);
    }

    @GetMapping("/{id}/getName")
    public String getName(@PathVariable Integer id){
        return employeeService.getName(id);
    }

    @GetMapping("/{id}/getRole")
    public String getRole(@PathVariable Integer id){
        return employeeService.getRole(id);
    }

    @GetMapping("/{id}/getDepartment")
    public String getDepartment(@PathVariable Integer id){
        return employeeService.getDepartment(id);
    }

    @GetMapping("/{id}/getSalary")
    public Double getSalary(@PathVariable Integer id){
        return employeeService.getSalary(id);
    }

    @GetMapping("/{id}/getHireDate")
    public LocalDate getHireDate(@PathVariable Integer id){
        return employeeService.getHireDate(id);
    }

    @GetMapping("/{id}/getDesignation")
    public String getDesignation(@PathVariable Integer id){
        return employeeService.getDesignation(id);
    }

    @GetMapping("/{id}/getStatus")
    public String getStatus(@PathVariable Integer id){
        return employeeService.getStatus(id);
    }

    @GetMapping("/{id}/getManager")
    public Employee getManager(@PathVariable Integer id){
        return employeeService.getManager(id);
    }
}