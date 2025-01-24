package com.dk13.storageservice.responses;

import java.util.List;

public class UsersInfoResponse {
    private List<UserInfo> users;
    
    public List<UserInfo> getUsers() {
        return users;
    }
    
    public UsersInfoResponse(List<UserInfo> users) {
        this.users = users;
    }
}
