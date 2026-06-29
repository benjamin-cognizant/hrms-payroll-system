package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EController {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private EmployeeService employeeService;

    public void saveData(Employee employee){
        employeeRepo.save(employee);
    }

    public Employee findData(@PathVariable int id){

        return employeeService.findData(id);
    }
}
