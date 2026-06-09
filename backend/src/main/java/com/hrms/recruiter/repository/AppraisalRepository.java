package com.hrms.recruiter.repository;

import com.hrms.recruiter.model.AppraisalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppraisalRepository extends JpaRepository<AppraisalRecord,Integer> {
}
