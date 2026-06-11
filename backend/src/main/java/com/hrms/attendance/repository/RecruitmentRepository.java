package com.hrms.attendance.repository;


import com.cognizant.hrms.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Candidate,Integer> {
}
