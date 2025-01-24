package com.dk13.storageservice.configuration.other;

import com.dk13.storageservice.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecureUserDetails implements UserDetails {
    
    private final String username;
    private final String password;
    List<SimpleGrantedAuthority> authorities;
    
    public SecureUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .toList();
    }
    
    public List<String> getAuthoritiesList() {
        return authorities.stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
