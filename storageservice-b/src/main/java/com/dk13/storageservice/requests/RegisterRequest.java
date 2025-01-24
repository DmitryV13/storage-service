package com.dk13.storageservice.requests;

public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private String password;
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getLogin() {
        return login;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
