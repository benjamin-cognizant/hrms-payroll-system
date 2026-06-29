package com.hrms.recruiter.repository;

import com.hrms.recruiter.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {
    List<Interview> findByCandidateId(Integer candidateId);
}

