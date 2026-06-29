@echo off
REM HRMS Recruitment Module - Startup Script
REM This script starts the Spring Boot application

echo ========================================
echo HRMS - Recruitment Module
echo ========================================
echo.
echo [1/3] Checking prerequisites...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install JDK 21 or higher
    pause
    exit /b 1
)
echo ✓ Java found

REM Check if MySQL is running
netstat -an | find "3306" >nul
if %errorlevel% neq 0 (
    echo WARNING: MySQL may not be running on port 3306
    echo Please start MySQL before continuing
    echo Press Ctrl+C to cancel or
    pause
)
echo ✓ MySQL appears to be running

echo.
echo [2/3] Starting Spring Boot Application...
echo.
echo This may take 30-60 seconds...
echo Please wait for "Started HrmsApplication" message
echo.

cd /d "%~dp0"
call mvnw.cmd spring-boot:run

echo.
echo [3/3] Application stopped.
pause


