package com.dk13.storageservice.services;

import com.dk13.storageservice.configuration.other.SecureUserDetails;
import com.dk13.storageservice.configuration.properties.MinioProperties;
import com.dk13.storageservice.responses.DownloadFileResponse;
import com.dk13.storageservice.entities.StorageFile;
import com.dk13.storageservice.exceptions.ReservationException;
import com.dk13.storageservice.exceptions.StorageFileException;
import com.dk13.storageservice.repositories.StorageFileRepository;
import com.dk13.storageservice.repositories.UserRepository;
import com.dk13.storageservice.requests.FileRequest;
import com.dk13.storageservice.responses.FileInfoResponse;
import io.minio.*;
import io.minio.http.Method;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class FileService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final StorageFileRepository storageFileRepository;
    private final UserService userService;
    private final ReservationService reservationService;
    private final EmailService emailService;
    
    public FileService(
            MinioClient minioClient,
            MinioProperties minioProperties,
            StorageFileRepository storageFileRepository,
            UserService userService,
            ReservationService reservationService,
            EmailService emailService
            ) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        this.storageFileRepository = storageFileRepository;
        this.userService = userService;
        this.reservationService = reservationService;
        this.emailService = emailService;
    }
    
    public String uploadFile(MultipartFile file) {
        try{
            reservationService.createBucket();
        }
        catch(Exception e){
            throw new StorageFileException("File upload exception(couldn't create bucket)!" + e.getMessage());
        }
        if(
                file.isEmpty() ||
                file.getOriginalFilename() == null ||
                file.getOriginalFilename().isEmpty()
        ){
            throw new StorageFileException("File upload exception!");
        }
        String fileName = generateFileName(file);
        InputStream inputStream;
        try{
            inputStream = file.getInputStream();
        }catch(Exception e){
            throw new StorageFileException("File upload exception(inputStream)!" + e.getMessage());
        }
        
        var user = userService.getUserFromContext();
        
        if(user.getUserReservation().getTotalSize() <
                user.getUserReservation().getUsedSize() + file.getSize()){
            throw new ReservationException(
                    "File can't be uploaded, it is not enough allocated space!"
            );
        }
        saveFile(inputStream, fileName);
        
        StorageFile storageFile = new StorageFile.builder()
                .creationDate(new Date())
                .name(fileName)
                .size(file.getSize())
                .mimeType(file.getContentType())
                .path(getMinioFilePath(minioProperties.getBucket(), fileName))
                .user(user)
                .build();
        
        user.getUserReservation().addUsedSize(storageFile.getSize());
        var totalSize = user.getUserReservation().getTotalSize();
        var usedSize = user.getUserReservation().getUsedSize();
        
        emailService.sendTemplateMail(
                user,
                "Storage Service / File was uploaded",
                usedSize.toString()+"b / "+totalSize.toString()+"b",
                "file_uploaded.html");
        
        storageFileRepository.save(storageFile);
        userService.saveUser(user);
        return fileName;
    }
    
    
    
    private String generateFileName(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return UUID.randomUUID().toString() + "." + extension;
    }
    
    private void saveFile(InputStream inputStream, String fileName) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .stream(inputStream, inputStream.available(), -1)
                    .bucket(minioProperties.getBucket())
                    .object(fileName)
                    .build());
        }catch(Exception e){
            throw new StorageFileException("File upload exception!" + e.getMessage());
        }
    }
    
    public DownloadFileResponse downloadFile(FileRequest request) {
        var user = userService.getUserFromContext();
        var storageFiles = storageFileRepository.findAllByUserId(user.getId());
        var fileToDownload = storageFileRepository.findById(request.getFileId()).get();
        if(!storageFiles.contains(fileToDownload)) {
            throw new StorageFileException("File not found!");
        }
        return new DownloadFileResponse(
                fileToDownload.getName(),
                fileToDownload.getMimeType(),
                getFile(fileToDownload.getName())
        );
    }
    
    private InputStream getFile(String fileName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new StorageFileException("File download exception: " + e.getMessage());
        }
    }
    
    private String getMinioFilePath(String bucketName, String objectName) {
        String minioUrl = minioProperties.getUrl();
        return String.format("%s/%s/%s", minioUrl, bucketName, objectName);
    }
    
    public String generatePresignedUrl(FileRequest request) {
        var user = userService.getUserFromContext();
        var storageFiles = storageFileRepository.findAllByUserId(user.getId());
        var fileToDownload = storageFileRepository.findById(request.getFileId()).get();
        if(!storageFiles.contains(fileToDownload)) {
            throw new StorageFileException("File not found!");
        }
        return generateDownloadUrl(fileToDownload.getName());
    }
    
    private String generateDownloadUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(fileName)
                            .method(Method.GET)
                            .expiry((int) TimeUnit.MINUTES.toSeconds(3600)) //link is active 1 hour
                            .build()
            );
        } catch (Exception e) {
            throw new ReservationException("Error generating pre-signed URL: " + e.getMessage());
        }
    }
    
    public String deleteFile(FileRequest request) {
        var user = userService.getUserFromContext();
        var storageFiles = storageFileRepository.findAllByUserId(user.getId());
        var fileToDelete = storageFileRepository.findById(request.getFileId()).get();
        if(!storageFiles.contains(fileToDelete)) {
            throw new StorageFileException("File not found!");
        }
        user.getUserReservation().addUsedSize(-fileToDelete.getSize());
        
        deleteFileFromStorage(fileToDelete.getName());
        
        storageFileRepository.delete(fileToDelete);
        userService.saveUser(user);
        return "File deleted successfully.";
    }
    
    private void deleteFileFromStorage(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new ReservationException("Error while deleting file: " + e.getMessage());
        }
    }
    
    public FileInfoResponse getFileInfo(FileRequest request) {
        var user = userService.getUserFromContext();
        var storageFiles = storageFileRepository.findAllByUserId(user.getId());
        var fileToFind = storageFileRepository.findById(request.getFileId()).get();
        if(!storageFiles.contains(fileToFind)) {
            throw new StorageFileException("File not found!");
        }
        return new FileInfoResponse.Builder()
                .setCreationDate(fileToFind.getCreationDate())
                .setSize(fileToFind.getSize())
                .setMimeType(fileToFind.getMimeType())
                .setName(fileToFind.getName())
                .setPath(fileToFind.getPath())
                .build();
    }
    
}
