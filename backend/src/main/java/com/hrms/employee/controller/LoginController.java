package com.hrms.employee.controller;

import com.hrms.employee.dto.LoginResponse;
import com.hrms.employee.model.User;
import com.hrms.employee.security.JwtUtil;
import com.hrms.employee.service.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserDetailsService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(UserDetailsService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        log.info("Login attempt for user: {}", username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            User user = userService.findByUsername(username);
            String role = user.getRole();
            String redirectUrl;

            if ("ROLE_EMPLOYEE".equals(role)) {
                redirectUrl = "/employee.html";
            } else if ("ROLE_HR_RECRUITER".equals(role)) {
                redirectUrl = "/hr_recruiter.html";
            } else if ("ROLE_PAYROLL_OFFICER".equals(role)) {
                redirectUrl = "/payroll.html";
            } else if ("ROLE_MANAGER".equals(role)) {
                redirectUrl = "/manager.html";
            } else if ("ROLE_ADMIN".equals(role)) {
                redirectUrl = "/admin.html";
            } else {
                redirectUrl = "/index.html"; // fallback if role is unknown
            }

            log.info("User logged in successfully: {}", username);
            return ResponseEntity.ok(new LoginResponse(token, redirectUrl, role));
        } catch (AuthenticationException e) {
            log.warn("Invalid login attempt for user: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, "Invalid username or password"));
        } catch (Exception e) {
            log.error("Error during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(null, null, "An error occurred during login"));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken() {
        log.info("Token validation endpoint accessed");
        return ResponseEntity.ok(new LoginResponse(null, null, "Token is valid"));
    }
}
