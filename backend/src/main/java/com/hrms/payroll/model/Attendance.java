package com.hrms.payroll.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "attendance$leave_request")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="leave_id")
    private long leaveId;

    @Column(name = "employee_id")
    private long employeeId;

    @Column(name = "leave_type")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please select one type of leave")
    private Leave leaveType;

    @Column(name = "from_date")
    @NotNull(message = "Please select a start date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    @NotNull(message = "Please select an end date")
    @Future(message = "To date must be in the future")
    private LocalDate toDate;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;


    public long getLeaveId() { return leaveId; }
    public void setLeaveId(long leaveId) { this.leaveId = leaveId; }

    public long getEmployeeId() { return employeeId; }
    public void setEmployeeId(long employeeId) { this.employeeId = employeeId; }

    public Leave getLeaveType() { return leaveType; }
    public void setLeaveType(Leave leaveType) { this.leaveType = leaveType; }

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
}
