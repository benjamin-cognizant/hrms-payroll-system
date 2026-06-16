package com.genc.hrms.controller;

import com.genc.hrms.model.LeaveRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/leave")
public class AttendanceController {

    @GetMapping
    public String home(){
        return "Employee";
    }
    @GetMapping("/apply")
    public String applyLeaveForm(){
        return "ApplyLeave";
    }
    @GetMapping("/leavePage")
    public String leaveForm(){
        return "ApplyLeaveForm";
    }
    @PostMapping("/applyLeaveform")
    public String applyLeave(@Valid @ModelAttribute LeaveRequest leaveRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "ApplyLeaveForm";
        }
         return "Success";
    }
    @GetMapping("/view")
    public String viewLeaveHistory() {
        return "ViewLeaveHistory";
    }

    @GetMapping("/submit")
    public String submitsheet(){
        return "SubmitTimeSheet";
    }
    @PostMapping("/submitTimeSheet")
    public String submitTimeSheet(){
        return "Success";
    }
    
    @GetMapping("/markAttendance")
    public String markAttendance(){

        return "95";
    }
    @GetMapping("/viewAttendance")
    public String viewAttendance(){
        return "ViewAttendance";
    }
    ////    Manager
//    public String approveLeave(){
//        return "jay";
//    }
}

