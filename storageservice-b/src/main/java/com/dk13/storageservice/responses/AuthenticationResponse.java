package com.dk13.storageservice.responses;

import java.util.List;

public class AuthenticationResponse {
    private String token;
    private List<String> authorities;
    
    private AuthenticationResponse(builder builder) {
        this.token = builder.token;
        this.authorities = builder.authorities;
    }
    
    public String getToken() {
        return token;
    }
    
    public List<String> getAuthorities() {
        return authorities;
    }
    
    public static class builder{
        private String token;
        private List<String> authorities;
        
        public builder authorities (List<String> authorities){
            this.authorities = authorities;
            return this;
        }
        
        public builder token(String token){
            this.token = token;
            return this;
        }
        
        public AuthenticationResponse build(){
            return new AuthenticationResponse(this);
        }
    }
}
