package com.dk13.storageservice.services;

import com.dk13.storageservice.configuration.properties.MinioProperties;
import com.dk13.storageservice.entities.UserReservation;
import com.dk13.storageservice.exceptions.MailException;
import com.dk13.storageservice.exceptions.ReservationException;
import com.dk13.storageservice.exceptions.UserException;
import com.dk13.storageservice.repositories.ReservationRepository;
import com.dk13.storageservice.repositories.UserRepository;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
public class ReservationService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final EmailService emailService;
    
    public ReservationService(
            MinioClient minioClient,
            MinioProperties minioProperties,
            ReservationRepository reservationRepository,
            UserService userService,
            EmailService emailService
    ) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.emailService = emailService;
    }
    
    public void createBucket() {
        try {
            boolean created = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
            if (!created) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .build());
            }
        }catch(Exception e){
            throw new ReservationException("Minio exception ( createBucket() ). " + e.getMessage());
        }
    }
    
    public void createReservation(Long userId, Long totalSize) {
        var user = userService.getUserById(userId);
        UserReservation userReservation = new UserReservation.builder()
                .activated(true)
                .totalSize(totalSize)
                .usedSize(0L)
                .createdBy(user.getUsername())
                .createdDate(new Date())
                .user(user)
                .build();
        
        reservationRepository.save(userReservation);
    }
    
    public void changeReservationState(String username, Boolean state) {
        var user = userService.getUserByLogin(username);
        
        if(user.getUserReservation()==null){
            throw new ReservationException("Reservation was not found!");
        }
        user.getUserReservation().setActivated(state);
        
        emailService.sendTemplateMail(
                user,
                "Storage Service / Reservation State",
                (state ? "unblocked" : "blocked"),
                "reservation_state.html");
        
        userService.saveUser(user);
    }
}
