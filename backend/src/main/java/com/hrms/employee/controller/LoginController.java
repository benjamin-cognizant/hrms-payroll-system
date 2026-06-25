package com.hrms.employee.controller;

import com.hrms.employee.model.User;
import com.hrms.employee.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String loginname = credentials.get("username");
        String password  = credentials.get("password");
        String role      = credentials.get("role");

        User user = loginService.authenticate(loginname, password);

        if (user == null) {
            return new ResponseEntity<>(Map.of("error", "Invalid username or password"), HttpStatus.UNAUTHORIZED);
        }

        if (!user.getRole().equalsIgnoreCase(role)) {
            return new ResponseEntity<>(Map.of("error", "Role mismatch"), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(Map.of(
                "message", "Login successful",
                "username", user.getLoginname(),
                "role", user.getRole()
        ), HttpStatus.OK);
    }
}
