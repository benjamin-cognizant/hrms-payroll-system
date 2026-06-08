package com.hrms.employee.repository;


import com.hrms.employee.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Candidate,Integer> {
}
