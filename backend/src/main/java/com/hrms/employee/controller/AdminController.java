package com.hrms.employee.controller;

import com.hrms.employee.model.User;
import com.hrms.employee.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AdminController {

    private final UserDetailsService userDetailsService;

    @Autowired
    public AdminController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = userDetailsService.saveUser(user);
        return ResponseEntity.ok(saved);
    }
}
