package com.genc.hrms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="leave_id")
    private long leaveId;
//    @ManyToOne
//    @JoinColumn(name = "employee_id")
//    private Employee employee;
    @Column(name = "leave_type")
    @Enumerated
    private Leave leaveType;

    @Column(name = "from_date")
    @FutureOrPresent(message = "From date must be today or in the future")
    @NotNull(message = "Please select a start date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    @NotNull(message = "Please select a end date")
    @Future(message = "To date must be in the future")
    private LocalDate toDate;

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    @Column(name = "status")
    @Enumerated
    private Status status;

    public long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(long leaveId) {
        this.leaveId = leaveId;
    }

    public Leave getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Leave leaveType) {
        this.leaveType = leaveType;
    }



    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
//    https://excalidraw.com/#json=lEbZULDfTdhYsTCUrl-_M,5zPhdTj_VeAzM_A-InM7UQ
}
