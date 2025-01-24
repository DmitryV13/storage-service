package com.dk13.storageservice.services;

import com.dk13.storageservice.configuration.other.SecureUserDetails;
import com.dk13.storageservice.entities.UserReservation;
import com.dk13.storageservice.exceptions.AllocationException;
import com.dk13.storageservice.repositories.ReservationRepository;
import com.dk13.storageservice.repositories.StorageFileRepository;
import com.dk13.storageservice.repositories.UserRepository;
import com.dk13.storageservice.requests.AllocationRequest;
import com.dk13.storageservice.responses.ProfileResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProfileService {
    private final UserService userService;
    private final StorageFileRepository storageFileRepository;
    private final ReservationRepository reservationRepository;
    
    public ProfileService(
            UserService userService,
            StorageFileRepository storageFileRepository,
            ReservationRepository reservationRepository
    ) {
        this.userService = userService;
        this.storageFileRepository = storageFileRepository;
        this.reservationRepository = reservationRepository;
    }
    
    public ProfileResponse getProfile() {
        var user = userService.getUserFromContext();
        var storageFiles = storageFileRepository.findAllByUserId(user.getId());
        return new ProfileResponse(user, storageFiles);
    }
    
    public String allocate(AllocationRequest request){
        var user = userService.getUserFromContext();
        
        if(user.getUserReservation()!=null){
            throw new AllocationException(
                    user.getUserReservation().getTotalSize().toString() +
                            " mb were already requested to allocate. Please wait.");
        }
        var reservation = new UserReservation.builder()
                .activated(false)
                .totalSize(request.getAllocatedSpace() * 1024 * 1024)//in mbytes
                .usedSize(0L)
                .createdBy(user.getUsername())
                .createdDate(new Date())
                .build();
        reservation.setUser(user);
        reservationRepository.save(reservation);
        return request.getAllocatedSpace().toString() + "mb was requested. Please wait.";
    }
}
