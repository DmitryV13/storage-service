package com.dk13.storageservice.services;

import com.dk13.storageservice.configuration.other.SecureUserDetails;
import com.dk13.storageservice.entities.Authority;
import com.dk13.storageservice.entities.User;
import com.dk13.storageservice.exceptions.UserException;
import com.dk13.storageservice.repositories.UserRepository;
import com.dk13.storageservice.requests.AuthenticationRequest;
import com.dk13.storageservice.requests.RegisterRequest;
import com.dk13.storageservice.responses.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {
    
    
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    
    public AuthenticationService(
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            EmailService emailService
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }
    
    public String register(RegisterRequest request) {
        String activationKey = UUID.randomUUID().toString();
        
        var userAuthority = new Authority.builder()
                .name("USER")
                .build();
        var user = new User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .activated(false)
                .activationKey(activationKey)
                .build();
        user.getAuthorities().add(userAuthority);
        
        emailService.sendTemplateMail(
                user,
                "Storage Service / Account Confirmation",
                "http://localhost:3000/account-verification?key=" + activationKey,
                "email_verification.html");
        
        userService.saveUser(user);
        return "Confirmation message was send to your email.";
    }
    
    public String register(User user) {
        userService.saveUser(user);
        return "User registered successfully";
    }
    
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        var user = userService.getUserByLogin(request.getLogin());
        
        if(!user.getActivated()){
            throw new UserException("User is not activated");
        }
        //log.info("authorities  : {} ", user.getAuthoritiess());
        var secureUserDetails = new SecureUserDetails(user);
        var jwtToken = jwtService.generateToken(secureUserDetails);
        return new AuthenticationResponse.builder()
                .token(jwtToken)
                .authorities(secureUserDetails.getAuthoritiesList())
                .build();
    }
    
    public String confirmAccount(String key) {
        var user = userService.getUserByActivationKey(key);
        user.setActivated(true);
        user.setActivationKey(null);
        userService.saveUser(user);
        return "Your account was successfully confirmed.";
    }
}
