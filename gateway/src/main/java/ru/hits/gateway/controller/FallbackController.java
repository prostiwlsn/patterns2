package ru.hits.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    
    @GetMapping("/fallback/user")
    public ResponseEntity<String> userFallback() {
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body("User Service временно недоступен. Попробуйте позже.");
    }

    @GetMapping("/fallback/core")
    public ResponseEntity<String> coreFallback() {
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body("Core Service временно недоступен. Попробуйте позже.");
    }

    @GetMapping("/fallback/loan")
    public ResponseEntity<String> loanFallback() {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Loan Service временно недоступен. Попробуйте позже.");
    }
}