package com.hrms.employee.service;

import com.hrms.employee.model.Employee;
import com.hrms.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee) {
        if (employeeRepository.existsByEmployeeCode(employee.getEmployeeCode())) {
            throw new IllegalArgumentException(
                    "Employee code '" + employee.getEmployeeCode() + "' already exists.");
        }
        if (employee.getEmploymentStatus() == null) {
            employee.setEmploymentStatus(Employee.EmploymentStatus.ACTIVE);
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Integer employeeId, Employee updatedData) {
        Employee existing = findOrThrow(employeeId);
        existing.setFullName(updatedData.getFullName());
        existing.setDepartment(updatedData.getDepartment());
        existing.setDesignation(updatedData.getDesignation());
        existing.setEmploymentStatus(updatedData.getEmploymentStatus());
        return employeeRepository.save(existing);
    }

    public Employee assignManager(Integer employeeId, Integer managerId) {
        if (employeeId.equals(managerId)) {
            throw new IllegalArgumentException("An employee cannot be their own manager.");
        }
        Employee employee = findOrThrow(employeeId);
        Employee manager  = findOrThrow(managerId);
        employee.setManager(manager);
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(Integer employeeId) {
        return findOrThrow(employeeId);
    }

    private Employee findOrThrow(Integer employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Employee not found with ID: " + employeeId));
    }
}
