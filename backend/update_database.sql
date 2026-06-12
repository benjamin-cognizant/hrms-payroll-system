-- SQL script to fix the JobRequisition table description column
-- Run this in your MySQL database

USE hrms;

-- Update the description column to TEXT type
ALTER TABLE JobRequisition MODIFY COLUMN description TEXT;

-- Verify the change
DESCRIBE JobRequisition;

