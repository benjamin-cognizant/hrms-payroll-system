package com.hrms.attendance.repository;

import com.cognizant.hrms.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<LeaveRequest,Integer> {
}
