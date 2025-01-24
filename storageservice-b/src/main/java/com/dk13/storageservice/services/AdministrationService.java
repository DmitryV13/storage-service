package com.dk13.storageservice.services;

import com.dk13.storageservice.entities.User;
import com.dk13.storageservice.repositories.UserRepository;
import com.dk13.storageservice.responses.UserInfo;
import com.dk13.storageservice.responses.UsersInfoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministrationService{
    
    private final UserService userService;
    private final ReservationService reservationService;
    
    public AdministrationService(
            UserService userService,
            ReservationService reservationService
    ) {
        this.userService = userService;
        this.reservationService = reservationService;
    }
    
    public UsersInfoResponse getUsersInfo() {
        List<User> users = userService.getAllUsers();
        List<UserInfo> usersInfo = users.stream()
                .map(UserInfo::new)
                .collect(Collectors.toList());
        return new UsersInfoResponse(usersInfo);
    }
    
    public String unblockReservation(String username) {
        reservationService.changeReservationState(username, true);
        return "Reservation unblocked";
    }
    
    public String blockReservation(String username) {
        reservationService.changeReservationState(username, false);
        return "Reservation blocked";
    }
}
