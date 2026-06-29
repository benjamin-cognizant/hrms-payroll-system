package com.hrms.employee.controller;

import com.hrms.employee.model.User;
import com.hrms.employee.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService; // service to validate credentials

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String loginname,
                                   @RequestParam String password) {
        log.info("Login attempt for user: {}", loginname);

        User user = userService.authenticate(loginname, password);
        if (user == null) {
            log.warn("Invalid login for user: {}", loginname);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Decide redirect based on role
        if ("EMPLOYEE".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.ok("/employee.html");
        } else if ("HR_RECRUITER".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.ok("/hr_recruiter.html");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Role not supported");
        }
    }
}
