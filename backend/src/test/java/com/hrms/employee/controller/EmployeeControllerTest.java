package com.hrms.employee.controller;

import com.hrms.employee.model.Employee;
import com.hrms.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private Model model;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setRole("Developer");
        employee.setDepartment("IT");
        employee.setSalary(50000.0);
        employee.setHireDate(java.time.LocalDate.now());
    }

    @Test
    void testDashboard() {


    }

    @Test
    void testWrongDashboardName() {

    }

    @Test
    void testAddAttribute() {

    }
}
