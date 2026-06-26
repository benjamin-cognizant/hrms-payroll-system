package com.hrms.employee.service;

import com.hrms.employee.model.Employee;
import com.hrms.employee.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Create new employee
    public Employee createEmployee(Employee employee) {
        log.info("Creating new employee: {}", employee.getName());
        return employeeRepository.save(employee);
    }

    // Get employee by ID
    public Employee getEmployeeById(Integer id) {
        log.debug("Fetching employee with ID: {}", id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with ID: {}", id);
                    return new EntityNotFoundException("Employee not found with id " + id);
                });
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    // Update employee details
    public Employee updateEmployee(Integer id, Employee employee) {
        log.info("Updating employee with ID: {}", id);
        Employee existing = getEmployeeById(id);

        existing.setName(employee.getName());
        existing.setDepartment(employee.getDepartment());
        existing.setDesignation(employee.getDesignation());
        existing.setStatus(employee.getStatus());
        existing.setRole(employee.getRole());
        existing.setSalary(employee.getSalary());
        existing.setHireDate(employee.getHireDate());

        Employee updated = employeeRepository.save(existing);
        log.info("Employee updated successfully with ID: {}", updated.getId());
        return updated;
    }

    // Delete employee
    public void deleteEmployee(Integer id) {
        log.info("Deleting employee with ID: {}", id);
        Employee existing = getEmployeeById(id);
        employeeRepository.delete(existing);
    }

    // Assign a manager to an employee
    public Employee assignManager(Integer id, Integer managerId) {
        log.info("Assigning manager with ID {} to employee ID {}", managerId, id);
        Employee employee = getEmployeeById(id);
        Employee manager = getEmployeeById(managerId);

        employee.setManager(manager);
        Employee updated = employeeRepository.save(employee);

        log.info("Manager assigned successfully to employee ID {}", id);
        return updated;
    }

    // Individual field accessors (matching controller mappings)
    public Integer getId(Integer id) {
        return getEmployeeById(id).getId();
    }

    public String getName(Integer id) {
        return getEmployeeById(id).getName();
    }

    public String getRole(Integer id) {
        return getEmployeeById(id).getRole();
    }

    public String getDepartment(Integer id) {
        return getEmployeeById(id).getDepartment();
    }

    public Double getSalary(Integer id) {
        return getEmployeeById(id).getSalary();
    }

    public LocalDate getHireDate(Integer id) {
        return getEmployeeById(id).getHireDate();
    }

    public String getDesignation(Integer id) {
        return getEmployeeById(id).getDesignation();
    }

    public String getStatus(Integer id) {
        return getEmployeeById(id).getStatus();
    }

    public Employee getManager(Integer id) {
        return getEmployeeById(id).getManager();
    }
}
