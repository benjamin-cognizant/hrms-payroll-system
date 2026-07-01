package com.hrms.payroll.repository;

import com.hrms.payroll.model.Attendance;
import com.hrms.payroll.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByEmployeeIdAndType(long employeeId, Type type);}
