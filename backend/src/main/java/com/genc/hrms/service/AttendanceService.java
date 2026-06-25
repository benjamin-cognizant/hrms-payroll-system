package com.genc.hrms.service;

import com.genc.hrms.model.*;
import com.genc.hrms.repository.AttendanceRepository;
import com.genc.hrms.repository.AvailableLeavesRepository;
import com.genc.hrms.repository.MarkAttendanceRepository;
import org.aspectj.apache.bcel.classfile.InnerClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private AvailableLeavesRepository availableLeavesRepository;

    @Autowired
    private MarkAttendanceRepository markAttendanceRepository;


    @Value("${hrms.leave.standard-annual-days}")
    private int standardAnnualLeaveDays;

    // for employee saving leave requests
    public void saveLeaveRequest(LeaveRequest leaveRequest)
    {
        long totalDays = ChronoUnit.DAYS.between(leaveRequest.getFromDate(), leaveRequest.getToDate()) + 1;
//        AvailableLeaves balance = availableLeavesRepository.findByEmployee_EmployeeId(leaveRequest.getEmployee().getEmployeeId());
        AvailableLeaves balance = availableLeavesRepository.findByEmployee_EmployeeId(3);

        if(totalDays<=standardAnnualLeaveDays && totalDays<= balance.getAvailableLeaves()) {
            leaveRequest.setStatus(Status.APPLIED);
            attendanceRepository.save(leaveRequest);
        }
        else {
//            exception or validation
        }
    }
//    viewing leave history
    public List<LeaveRequest> returnRequests(long id)
    {
        return attendanceRepository.findByEmployee_EmployeeId(id);
    }
// for leave summary
    public Map<String,Integer> viewSummary(long id)
    {
        Map<String,Integer> map=new HashMap<>();
        AvailableLeaves leaves=availableLeavesRepository.findByEmployee_EmployeeId(id);
        int total=standardAnnualLeaveDays;
        int available=(int)leaves.getAvailableLeaves();
        int used=total-available;
        map.put("TotalLeaves",total);
        map.put("Available",available);
        map.put("Used",used);
        return map;
    }
//    for marking attendance
    public void saveAttendance(MarkAttendance markAttendance)
    {
        long empId = markAttendance.getEmployee().getEmployeeId();
        LocalDate today = LocalDate.now();
        boolean alreadyCheckedIn = markAttendanceRepository.existsByEmployee_EmployeeIdAndPresentDate(empId, today);

        if (!alreadyCheckedIn) {
            markAttendance.setInTime(LocalTime.now());
            markAttendance.setPresentDate(today);
            markAttendance.setAttendanceStatus(AttendanceStatus.PENDING);
            markAttendanceRepository.save(markAttendance);
        }
    }
//    for marking timesheet
    public void saveTimeSheet(long id)
    {
        LocalDate today = LocalDate.now();
        MarkAttendance existingAttendance=markAttendanceRepository.findByEmployee_EmployeeIdAndPresentDate(id,today);
        existingAttendance.setOutTime(LocalTime.now());
        long totalHours = ChronoUnit.HOURS.between(existingAttendance.getInTime(), existingAttendance.getOutTime());
        existingAttendance.setTotalHours(totalHours);
        markAttendanceRepository.save(existingAttendance);
    }

    public MarkAttendance getTodayAttendance(long id) {
        LocalDate today = LocalDate.now();
        return markAttendanceRepository.findByEmployee_EmployeeIdAndPresentDate(id, today);
    }

//    for manager
    public List<LeaveRequest> returnApplied()
    {
        return attendanceRepository.findByStatus(Status.APPLIED);
    }

        public void approveLeaveRequest(long leaveId) {
            LeaveRequest existingRequest = attendanceRepository.findById(leaveId).orElse(null);
            AvailableLeaves leaves=availableLeavesRepository.findByEmployee_EmployeeId(existingRequest.getEmployee().getEmployeeId());
            long totalDays = ChronoUnit.DAYS.between(existingRequest.getFromDate(), existingRequest.getToDate()) + 1;
            long available=leaves.getAvailableLeaves()-totalDays;
            leaves.setAvailableLeaves(available);
            availableLeavesRepository.save(leaves);
            existingRequest.setStatus(Status.APPROVED);
            attendanceRepository.save(existingRequest);
        }

        public void rejectLeaveRequest(long leaveId) {
            LeaveRequest existingRequest = attendanceRepository.findById(leaveId).orElse(null);
            existingRequest.setStatus(Status.REJECTED);
            attendanceRepository.save(existingRequest);
        }
    public List<MarkAttendance> returnTimeSheet()
    {
        return markAttendanceRepository.findByAttendanceStatus(AttendanceStatus.PENDING);
    }



}
