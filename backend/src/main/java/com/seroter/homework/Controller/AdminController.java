package com.seroter.homework.Controller;

import com.seroter.homework.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthService authService;

    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard(HttpSession session) {
        if (!authService.isAuthenticated(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in.");
        }

        if (authService.hasRole(session, "Student")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        return ResponseEntity.ok("Welcome to Admin Dashboard");
    }
}