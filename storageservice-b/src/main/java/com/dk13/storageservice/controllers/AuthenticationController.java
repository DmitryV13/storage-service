package com.dk13.storageservice.controllers;

import com.dk13.storageservice.requests.AuthenticationRequest;
import com.dk13.storageservice.requests.RegisterRequest;
import com.dk13.storageservice.responses.AuthenticationResponse;
import com.dk13.storageservice.services.AuthenticationService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(
            @RequestParam("key") String key
    ){
        return ResponseEntity.ok(authenticationService.confirmAccount(key));
    }
}
