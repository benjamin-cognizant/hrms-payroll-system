-- HRMS Database Schema for Recruitment Module
-- Create Database
CREATE DATABASE IF NOT EXISTS hrms;
USE hrms;

-- Candidate Table
CREATE TABLE IF NOT EXISTS Candidate (
    candidateId INT AUTO_INCREMENT PRIMARY KEY,
    fullName VARCHAR(100) NOT NULL,
    appliedRole VARCHAR(100) NOT NULL,
    experienceYears INT NOT NULL,
    interviewStage VARCHAR(50) NOT NULL,
    candidateStatus ENUM('APPLIED','IN_INTERVIEW','OFFERED','HIRED','REJECTED') NOT NULL,
    INDEX idx_status (candidateStatus),
    INDEX idx_role (appliedRole)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Job Requisition Table
CREATE TABLE IF NOT EXISTS JobRequisition (
    requisitionId INT AUTO_INCREMENT PRIMARY KEY,
    jobTitle VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    numberOfPositions INT NOT NULL,
    priority ENUM('LOW','MEDIUM','HIGH','URGENT') NOT NULL,
    status ENUM('OPEN','IN_PROGRESS','FILLED','CLOSED') NOT NULL,
    description TEXT,
    requisitionDate DATE NOT NULL,
    INDEX idx_status (status),
    INDEX idx_department (department)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Interview Table
CREATE TABLE IF NOT EXISTS Interview (
    interviewId INT AUTO_INCREMENT PRIMARY KEY,
    candidateId INT NOT NULL,
    interviewerName VARCHAR(100) NOT NULL,
    interviewDateTime DATETIME NOT NULL,
    interviewMode VARCHAR(50) NOT NULL,
    location VARCHAR(200),
    meetingLink VARCHAR(500),
    interviewRound VARCHAR(50) NOT NULL,
    interviewStatus ENUM('SCHEDULED','COMPLETED','CANCELLED','RESCHEDULED','NO_SHOW') NOT NULL,
    remarks VARCHAR(1000),
    FOREIGN KEY (candidateId) REFERENCES Candidate(candidateId) ON DELETE CASCADE,
    INDEX idx_candidate (candidateId),
    INDEX idx_date (interviewDateTime),
    INDEX idx_status (interviewStatus)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Offer Letter Table
CREATE TABLE IF NOT EXISTS OfferLetter (
    offerId INT AUTO_INCREMENT PRIMARY KEY,
    candidateId INT NOT NULL,
    positionOffered VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    salaryOffered DECIMAL(12,2) NOT NULL,
    offerDate DATE NOT NULL,
    joiningDate DATE NOT NULL,
    offerStatus ENUM('DRAFTED','SENT','ACCEPTED','REJECTED','WITHDRAWN') NOT NULL,
    additionalBenefits VARCHAR(1000),
    remarks VARCHAR(500),
    FOREIGN KEY (candidateId) REFERENCES Candidate(candidateId) ON DELETE CASCADE,
    INDEX idx_candidate (candidateId),
    INDEX idx_status (offerStatus),
    INDEX idx_offer_date (offerDate)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Employee Table (for future modules)
CREATE TABLE IF NOT EXISTS Employee (
    employeeId INT AUTO_INCREMENT PRIMARY KEY,
    employeeCode VARCHAR(20) UNIQUE NOT NULL,
    fullName VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    designation VARCHAR(100),
    managerId INT,
    employmentStatus ENUM('ACTIVE','ON_LEAVE','RESIGNED','TERMINATED') NOT NULL,
    FOREIGN KEY (managerId) REFERENCES Employee(employeeId) ON DELETE SET NULL,
    INDEX idx_status (employmentStatus),
    INDEX idx_department (department)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Leave Request Table (for future modules)
CREATE TABLE IF NOT EXISTS LeaveRequest (
    leaveId INT AUTO_INCREMENT PRIMARY KEY,
    employeeId INT NOT NULL,
    leaveType VARCHAR(50) NOT NULL,
    fromDate DATE NOT NULL,
    toDate DATE NOT NULL,
    leaveStatus ENUM('APPLIED','APPROVED','REJECTED','CANCELLED') NOT NULL,
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId) ON DELETE CASCADE,
    INDEX idx_employee (employeeId),
    INDEX idx_status (leaveStatus)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Payroll Record Table (for future modules)
CREATE TABLE IF NOT EXISTS PayrollRecord (
    payrollId INT AUTO_INCREMENT PRIMARY KEY,
    employeeId INT NOT NULL,
    payPeriod VARCHAR(20) NOT NULL,
    grossSalary DECIMAL(12,2) NOT NULL,
    totalDeductions DECIMAL(12,2) NOT NULL,
    netSalary DECIMAL(12,2) NOT NULL,
    payrollStatus ENUM('DRAFT','PROCESSED','PAID','ON_HOLD') NOT NULL,
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId) ON DELETE CASCADE,
    INDEX idx_employee (employeeId),
    INDEX idx_period (payPeriod),
    INDEX idx_status (payrollStatus)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Appraisal Record Table (for future modules)
CREATE TABLE IF NOT EXISTS AppraisalRecord (
    appraisalId INT AUTO_INCREMENT PRIMARY KEY,
    employeeId INT NOT NULL,
    appraisalCycle VARCHAR(20) NOT NULL,
    goalsAchieved INT,
    overallRating DECIMAL(3,1),
    appraisalStatus ENUM('DRAFT','SELF_REVIEW','MANAGER_REVIEW','PUBLISHED') NOT NULL,
    FOREIGN KEY (employeeId) REFERENCES Employee(employeeId) ON DELETE CASCADE,
    INDEX idx_employee (employeeId),
    INDEX idx_cycle (appraisalCycle),
    INDEX idx_status (appraisalStatus)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sample Data for Testing

-- Insert Sample Candidates
INSERT INTO Candidate (fullName, appliedRole, experienceYears, interviewStage, candidateStatus) VALUES
('Rajesh Kumar', 'Software Engineer', 5, 'Initial Screening', 'APPLIED'),
('Priya Sharma', 'Senior Developer', 8, 'Technical Round', 'IN_INTERVIEW'),
('Amit Patel', 'Team Lead', 10, 'Offer Sent', 'OFFERED'),
('Neha Gupta', 'Junior Developer', 2, 'HR Round', 'IN_INTERVIEW');

-- Insert Sample Job Requisitions
INSERT INTO JobRequisition (jobTitle, department, numberOfPositions, priority, status, description, requisitionDate) VALUES
('Full Stack Developer', 'Engineering', 3, 'HIGH', 'OPEN', 'Need experienced full stack developers with React and Spring Boot', '2026-07-01'),
('QA Engineer', 'Quality Assurance', 2, 'MEDIUM', 'IN_PROGRESS', 'Automation testing experience required', '2026-06-25'),
('DevOps Engineer', 'Operations', 1, 'URGENT', 'OPEN', 'AWS and Kubernetes experience mandatory', '2026-06-28');

-- Insert Sample Interviews
INSERT INTO Interview (candidateId, interviewerName, interviewDateTime, interviewMode, location, interviewRound, interviewStatus, remarks) VALUES
(2, 'Suresh Menon', '2026-06-25 10:00:00', 'Online', NULL, 'Technical', 'SCHEDULED', 'Focus on React and Spring Boot'),
(4, 'Kavita Singh', '2026-06-24 14:00:00', 'Offline', 'Bangalore Office', 'HR', 'COMPLETED', 'Good communication skills');

-- Insert Sample Offers
INSERT INTO OfferLetter (candidateId, positionOffered, department, salaryOffered, offerDate, joiningDate, offerStatus, additionalBenefits, remarks) VALUES
(3, 'Team Lead', 'Engineering', 1500000.00, '2026-06-20', '2026-07-15', 'SENT', 'Health Insurance, Stock Options', 'Waiting for candidate response');

-- Display Tables
SELECT 'Candidates:' AS '';
SELECT * FROM Candidate;

SELECT 'Job Requisitions:' AS '';
SELECT * FROM JobRequisition;

SELECT 'Interviews:' AS '';
SELECT * FROM Interview;

SELECT 'Offers:' AS '';
SELECT * FROM OfferLetter;

