package com.cognizant.hrms.repository;

import com.cognizant.hrms.model.AppraisalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppraisalRepository extends JpaRepository<AppraisalRecord,Integer> {
}
