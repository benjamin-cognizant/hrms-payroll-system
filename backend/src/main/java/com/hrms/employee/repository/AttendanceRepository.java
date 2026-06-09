package com.hrms.employee.repository;

import com.hrms.employee.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<LeaveRequest,Integer> {
}
