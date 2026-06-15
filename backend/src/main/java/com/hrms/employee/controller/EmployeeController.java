package com.hrms.employee.controller;



import com.hrms.employee.model.Employee;
import com.hrms.employee.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        Employee created = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id,
                                                   @Valid @RequestBody Employee employee) {
        Employee updated = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/manager/{managerId}")
    public ResponseEntity<Employee> assignManager(@PathVariable Integer id,
                                                  @PathVariable Integer managerId) {
        Employee updated = employeeService.assignManager(id, managerId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeDetails(@PathVariable Integer id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
}
