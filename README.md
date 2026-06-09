This is a readme for the employee payroll system.
This is a another commit change.
This is a another commit change from JAYAKUMAR

This is a another commit change from Satish.

This is a another commit change from Divya 

Recruitment & Candidate Onboarding Module--->
 
1) createJobRequisition - CREATE TABLE Candidate (

candidateId INT AUTO_INCREMENT PRIMARY KEY,

fullName VARCHAR(100),

appliedRole VARCHAR(100),

experienceYears DECIMAL(4,1),

interviewStage VARCHAR(50),

candidateStatus ENUM('APPLIED','IN_INTERVIEW','OFFERED','HIRED','REJECTED'),
RoleDesc varchar(100)

)
create the job and we have to implement these in form .
 
 
2) addCandidate- so in this hr will add the candidate based on  enum of the candidate like applied -- > schedule interview , offered->> we have to create this module for upcoming joining letter  .  hired->> create employee of next module.
rejected- rejected.
 
3) scheduleInterview- schedule interview for the applied or in_interview .
 
4)- rollout offer()- candidates who got offered then we need to give joining date or cleared in interview then give them offer . 
 
