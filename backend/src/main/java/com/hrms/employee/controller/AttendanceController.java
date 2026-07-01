package com.hrms.employee.controller;

import com.hrms.employee.model.LeaveRequest;
import com.hrms.employee.model.Timesheet;
import com.hrms.employee.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: Edit the hr recruiter frontend and payroll frontend in the morning

@RestController
@RequestMapping("/api/leave")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Apply for leave
    @PostMapping("/apply")
    public ResponseEntity<LeaveRequest> applyLeave(@Valid @RequestBody LeaveRequest leaveRequest) {
        LeaveRequest saved = attendanceService.applyLeave(leaveRequest);
        return ResponseEntity.ok(saved);
    }

    // Approve leave
    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequest> approveLeave(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.approveLeave(id));
    }

    // Mark attendance
    @GetMapping("/markAttendance")
    public ResponseEntity<String> markAttendance() {
        String result = attendanceService.markAttendance();
        return ResponseEntity.ok(result);
    }

    // Submit timesheet
    @PostMapping("/submitTimesheet")
    public ResponseEntity<Timesheet> submitTimesheet(@Valid @RequestBody Timesheet timesheet) {
        Timesheet saved = attendanceService.submitTimesheet(timesheet);
        return ResponseEntity.ok(saved);
    }

    // NEW: View all leave requests
    @GetMapping("/view")
    public ResponseEntity<List<LeaveRequest>> viewLeaveHistory() {
        return ResponseEntity.ok(attendanceService.getAllLeaveRequests());
    }
}
