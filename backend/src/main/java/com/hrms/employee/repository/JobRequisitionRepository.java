package com.hrms.employee.repository;

import com.hrms.employee.model.JobRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRequisitionRepository extends JpaRepository<JobRequisition, Integer> {
}

