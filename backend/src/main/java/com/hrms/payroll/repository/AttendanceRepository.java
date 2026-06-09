package com.hrms.payroll.repository;

import com.hrms.payroll.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<LeaveRequest,Integer> {
}
