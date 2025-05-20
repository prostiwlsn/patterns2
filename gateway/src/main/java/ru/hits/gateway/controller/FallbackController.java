package ru.hits.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @RequestMapping(path = "/fallback/user", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.status(503).body("User Service временно недоступен");
    }

    @RequestMapping(path = "/fallback/core", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> coreFallback() {
        return ResponseEntity.status(503).body("Core Service временно недоступен");
    }

    @RequestMapping(path = "/fallback/loan", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> loanFallback() {
        return ResponseEntity.status(503).body("Loan Service временно недоступен");
    }
}