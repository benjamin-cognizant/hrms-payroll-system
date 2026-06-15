package com.hrms.employee.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Employee")
public class Employee {

    public enum EmploymentStatus {
        ACTIVE, ON_LEAVE, RESIGNED, TERMINATED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employeeId")
    private Integer employeeId;

    @NotBlank(message = "Employee code is required")
    @Column(name = "employeeCode", unique = true, length = 20)
    private String employeeCode;

    @NotBlank(message = "Full name is required")
    @Column(name = "fullName", length = 100)
    private String fullName;

    @NotBlank(message = "Department is required")
    @Column(name = "department", length = 100)
    private String department;

    @NotBlank(message = "Designation is required")
    @Column(name = "designation", length = 100)
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managerId", referencedColumnName = "employeeId")
    private Employee manager;

    @NotNull(message = "Employment status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "employmentStatus")
    private EmploymentStatus employmentStatus;

    // Constructors
    public Employee() {}

    public Employee(String employeeCode, String fullName, String department,
                    String designation, Employee manager, EmploymentStatus employmentStatus) {
        this.employeeCode     = employeeCode;
        this.fullName         = fullName;
        this.department       = department;
        this.designation      = designation;
        this.manager          = manager;
        this.employmentStatus = employmentStatus;
    }

    // Getters & Setters
    public Integer getEmployeeId()                     { return employeeId; }
    public void    setEmployeeId(Integer employeeId)   { this.employeeId = employeeId; }

    public String getEmployeeCode()                    { return employeeCode; }
    public void   setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }

    public String getFullName()                        { return fullName; }
    public void   setFullName(String fullName)         { this.fullName = fullName; }

    public String getDepartment()                      { return department; }
    public void   setDepartment(String department)     { this.department = department; }

    public String getDesignation()                     { return designation; }
    public void   setDesignation(String designation)   { this.designation = designation; }

    public Employee getManager()                       { return manager; }
    public void     setManager(Employee manager)       { this.manager = manager; }

    public EmploymentStatus getEmploymentStatus()                        { return employmentStatus; }
    public void             setEmploymentStatus(EmploymentStatus status) { this.employmentStatus = status; }
}
