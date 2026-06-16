package com.genc.hrms.service;

import com.genc.hrms.model.LeaveRequest;
import com.genc.hrms.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    public void saveAttendance(LeaveRequest leaveRequest) {
        attendanceRepository.save(leaveRequest);
    }
    
    public LeaveRequest getAttendanceById(Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }
}
