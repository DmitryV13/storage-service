package com.dk13.storageservice.services;

import com.dk13.storageservice.configuration.other.SecureUserDetails;
import com.dk13.storageservice.entities.User;
import com.dk13.storageservice.exceptions.UserException;
import com.dk13.storageservice.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User getUserFromContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (SecureUserDetails)authentication.getPrincipal();
        return userRepository.findFirstByLogin(userDetails.getUsername()).orElseThrow(
                () -> new UserException("User not found")
        );
    }
    
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new UserException("User not found")
        );
    }
    
    public User getUserByLogin(String login){
        return userRepository.findByLogin(login).orElseThrow(
                () -> new UserException("User not found")
        );
    }
    
    public User getUserByActivationKey(String activationKey){
        return userRepository.findByActivationKey(activationKey).orElseThrow(
                () -> new UserException("User not found. Activation key may be incorrect.")
        );
    }
    
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
