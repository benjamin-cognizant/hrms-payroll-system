package com.hrms.payroll.repository;


import com.hrms.payroll.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Candidate,Integer> {
}
