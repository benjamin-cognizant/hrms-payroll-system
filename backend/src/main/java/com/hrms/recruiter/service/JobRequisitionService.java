package com.hrms.recruiter.service;

import com.hrms.recruiter.model.JobRequisition;
import com.hrms.recruiter.repository.JobRequisitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobRequisitionService {
    @Autowired
    private JobRequisitionRepository jobRequisitionRepository;

    /**
     * Creates a new job requisition
     * @param jobRequisition - The job requisition object to create
     * @return The saved job requisition
     */
    public JobRequisition createJobRequisition(JobRequisition jobRequisition) {
        // Set the requisition date to current date if not provided
        if (jobRequisition.getRequisitionDate() == null) {
            jobRequisition.setRequisitionDate(LocalDate.now());
        }
        // Save and return the job requisition
        return jobRequisitionRepository.save(jobRequisition);
    }

    /**
     * Get all job requisitions
     * @return List of all job requisitions
     */
    public List<JobRequisition> getAllJobRequisitions() {
        return jobRequisitionRepository.findAll();
    }

    /**
     * Get job requisition by ID
     * @param id - Requisition ID
     * @return JobRequisition object
     */
    public JobRequisition getJobRequisitionById(Integer id) {
        return jobRequisitionRepository.findById(id).orElse(null);
    }

    /**
     * Update job requisition
     * @param jobRequisition - Updated job requisition object
     * @return Updated job requisition
     */
    public JobRequisition updateJobRequisition(JobRequisition jobRequisition) {
        return jobRequisitionRepository.save(jobRequisition);
    }

    /**
     * Delete job requisition by ID
     * @param id - Requisition ID
     */
    public void deleteJobRequisition(Integer id) {
        jobRequisitionRepository.deleteById(id);
    }
}

