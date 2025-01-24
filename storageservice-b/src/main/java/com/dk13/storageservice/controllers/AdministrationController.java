package com.dk13.storageservice.controllers;

import com.dk13.storageservice.responses.UsersInfoResponse;
import com.dk13.storageservice.services.AdministrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdministrationController {
    private final AdministrationService administrationService;
    
    public AdministrationController(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }
    
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<UsersInfoResponse> usersInfo() {
        return ResponseEntity.ok(administrationService.getUsersInfo());
    }
    
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/unblock-reservation")
    public ResponseEntity<String> unblockReservation(
            @RequestParam("username") String username
    ) {
        return ResponseEntity.ok(administrationService.unblockReservation(username));
    }
    
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/block-reservation")
    public ResponseEntity<String> blockReservation(
            @RequestParam("username") String username
    ) {
        return ResponseEntity.ok(administrationService.blockReservation(username));
    }
}
