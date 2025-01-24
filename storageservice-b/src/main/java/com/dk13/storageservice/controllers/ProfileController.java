package com.dk13.storageservice.controllers;

import com.dk13.storageservice.exceptions.AllocationException;
import com.dk13.storageservice.exceptions.StorageFileException;
import com.dk13.storageservice.requests.AllocationRequest;
import com.dk13.storageservice.requests.FileRequest;
import com.dk13.storageservice.responses.FileInfoResponse;
import com.dk13.storageservice.responses.ProfileResponse;
import com.dk13.storageservice.services.FileService;
import com.dk13.storageservice.services.ProfileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final FileService fileService;
    
    public ProfileController(ProfileService profileService, FileService fileService) {
        this.profileService = profileService;
        this.fileService = fileService;
    }
    
    @GetMapping
    public ResponseEntity<ProfileResponse> profile() {
        return ResponseEntity.ok(profileService.getProfile());
    }
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            throw new StorageFileException("Transmitted file was empty!");
        }
        return ResponseEntity.ok(
                "File uploaded successfully: " + fileService.uploadFile(file)
        );
    }
    
    @PostMapping("/download-file")
    public ResponseEntity<InputStreamResource> downloadFileViaStream(
            @RequestBody FileRequest request
    ){
        var downloadFile = fileService.downloadFile(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""+ downloadFile.getFileName() +"\"")
                .contentType(MediaType.parseMediaType(downloadFile.getMimeType()))
                .body(new InputStreamResource(downloadFile.getInputStream()));
    }
    
    @PostMapping("/download-link")
    public ResponseEntity<String> downloadFileViaLink(
            @RequestBody FileRequest request
    ){
        return ResponseEntity.ok(fileService.generatePresignedUrl(request));
    }
    
    @PostMapping("/info")
    public ResponseEntity<FileInfoResponse> infoFile(
            @RequestBody FileRequest request
    ){
        return ResponseEntity.ok(fileService.getFileInfo(request));
    }
    
    @PostMapping("/delete")
    public ResponseEntity<String> deleteFile(
            @RequestBody FileRequest request
    ){
        return ResponseEntity.ok(fileService.deleteFile(request));
    }
    
    @PostMapping("/allocate")
    public ResponseEntity<String> allocateFile(
            @RequestBody AllocationRequest request
    ){
        if(
                request.getAllocatedSpace()!=100 &&
                request.getAllocatedSpace()!=200 &&
                request.getAllocatedSpace()!=300
        ){
            throw new AllocationException("Allocated space is wrong!");
        }
        return ResponseEntity.ok(profileService.allocate(request));
    }
}
