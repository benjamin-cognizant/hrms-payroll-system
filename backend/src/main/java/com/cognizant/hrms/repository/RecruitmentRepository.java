package com.cognizant.hrms.repository;

import com.cognizant.hrms.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Candidate, Integer> {

    
}
