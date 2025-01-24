package com.dk13.storageservice.requests;

public class AuthenticationRequest {
    private String login;
    private String password;
    
    public String getPassword() {
        return password;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
