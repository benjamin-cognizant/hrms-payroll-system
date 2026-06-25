package com.hrms.payroll.service;

import com.hrms.payroll.model.Payroll;
import com.hrms.payroll.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayrollService {

        @Autowired
        private PayrollRepository payrollRepository;

        public Payroll runPayroll(Payroll payroll) {

            double totalDeductions= deductions(payroll);
            payroll.setTotalDeductions(totalDeductions);

            double gross=payroll.getGrossSalary();
            payroll.setNetSalary(gross - totalDeductions);


            payrollRepository.save(payroll);
            return payroll;
        }
        public double deductions(Payroll payroll){
            double gross = payroll.getGrossSalary();

            double presentdays=24;
            double totaldays=30;
            double leaveDeduction = (gross / totaldays) * (totaldays - presentdays);
            double pf = gross * 0.12;
            double tds = gross * 0.05;
            double esi = gross * 0.03;

            return pf + tds + esi+leaveDeduction;
        }

        public List<Payroll> getData(){
            return payrollRepository.findAll();
        }


    public Payroll getLatestPayrollByEmployeeId(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId);
    }
    public void savePayroll(Payroll payroll) {
        payrollRepository.save(payroll);
    }

}
