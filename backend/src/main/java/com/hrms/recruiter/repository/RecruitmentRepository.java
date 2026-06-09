package com.hrms.recruiter.repository;

import com.hrms.recruiter.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Candidate, Integer> {

    
}
