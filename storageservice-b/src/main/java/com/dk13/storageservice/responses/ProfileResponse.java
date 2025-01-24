package com.dk13.storageservice.responses;

import com.dk13.storageservice.entities.StorageFile;
import com.dk13.storageservice.entities.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileResponse {
    private String firstName;
    private String lastName;
    private Boolean reservatonActivated;
    private List<StorageFileInfo> files;
    private List<String> authorities;
    private Long totalSize;
    private Long usedSize;
    
    public ProfileResponse(User user, List<StorageFile> files) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.authorities = user.getAuthoritiesList();
        if(user.getUserReservation() != null){
            this.reservatonActivated = user.getUserReservation().getActivated();
            this.files = files.stream()
                    .map(StorageFileInfo::new)
                    .collect(Collectors.toList());
            this.totalSize = user.getUserReservation().getTotalSize() / (1024*1024);
            this.usedSize = user.getUserReservation().getUsedSize() / (1024*1024);
        }
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public Boolean getReservatonActivated() {
        return reservatonActivated;
    }
    
    public List<StorageFileInfo> getFiles() {
        return files;
    }
    
    public Long getTotalSize() {
        return totalSize;
    }
    
    public Long getUsedSize() {
        return usedSize;
    }
    
    public List<String> getAuthorities() {
        return authorities;
    }
}
