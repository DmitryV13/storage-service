package com.dk13.storageservice.responses;

import com.dk13.storageservice.entities.User;

import java.util.List;

public class UserInfo {
    private String login;
    private List<String> authorities;
    private Boolean accountActivated;
    private String activationKey;
    private Boolean reservationActivated;
    private Long reservationTotalSize;
    private Long reservationUsedSize;
    
    public String getLogin() {
        return login;
    }
    
    public String getActivationKey() {
        return activationKey;
    }
    
    public Boolean getAccountActivated() {
        return accountActivated;
    }
    
    public List<String> getRole() {
        return authorities;
    }
    
    public Boolean getReservationActivated() {
        return reservationActivated;
    }
    
    public List<String> getAuthorities() {
        return authorities;
    }
    
    public Long getReservationUsedSize() {
        return reservationUsedSize;
    }
    
    public Long getReservationTotalSize() {
        return reservationTotalSize;
    }
    
    public UserInfo() {
    }
    
    public UserInfo(User user) {
        this.login = user.getUsername();
        this.authorities = user.getAuthoritiesList();
        this.accountActivated = user.getActivated();
        this.activationKey = user.getActivationKey();
        if(user.getUserReservation() != null) {
            this.reservationActivated = user.getUserReservation().getActivated();
            this.reservationTotalSize = user.getUserReservation().getTotalSize() / (1024*1024);
            this.reservationUsedSize = user.getUserReservation().getUsedSize() / (1024*1024);
        }
    }
}
