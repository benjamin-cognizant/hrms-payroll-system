package com.genc.hrms.repository;

import com.genc.hrms.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<LeaveRequest,Long> {
    
}
