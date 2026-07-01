package com.hrms.employee.service;

import com.hrms.employee.model.LeaveRequest;
import com.hrms.employee.model.Status;
import com.hrms.employee.model.Timesheet;
import com.hrms.employee.repository.AttendanceRepository;
import com.hrms.employee.repository.TimesheetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final TimesheetRepository timesheetRepository; // new repository for timesheets

    public AttendanceService(AttendanceRepository attendanceRepository,
                             TimesheetRepository timesheetRepository) {
        this.attendanceRepository = attendanceRepository;
        this.timesheetRepository = timesheetRepository;
    }

    // Apply for a new leave
    public LeaveRequest applyLeave(LeaveRequest leaveRequest) {
        leaveRequest.setStatus(Status.APPLIED);
        return attendanceRepository.save(leaveRequest);
    }

    // Approve a leave
    public LeaveRequest approveLeave(Long id) {
        LeaveRequest request = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        request.setStatus(Status.APPROVED);
        return attendanceRepository.save(request);
    }

    // Mark attendance
    public String markAttendance() {
        // Example logic: could record attendance in DB
        // For now, return a simple message
        return "Attendance marked successfully";
    }

    // Submit timesheet
    public Timesheet submitTimesheet(Timesheet timesheet) {
        // Save timesheet entry
        return timesheetRepository.save(timesheet);
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return attendanceRepository.findAll();
    }
}
