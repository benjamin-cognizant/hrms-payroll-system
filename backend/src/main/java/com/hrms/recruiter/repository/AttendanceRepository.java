package com.hrms.recruiter.repository;

import com.hrms.recruiter.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<LeaveRequest,Integer> {
}
