package com.dk13.storageservice.configuration.initializer;

import com.dk13.storageservice.entities.Authority;
import com.dk13.storageservice.entities.Role;
import com.dk13.storageservice.entities.User;
import com.dk13.storageservice.repositories.AuthorityRepository;
import com.dk13.storageservice.services.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements CommandLineRunner {
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    
    public UserInitializer(
            AuthenticationService authenticationService,
            PasswordEncoder passwordEncoder,
            AuthorityRepository authorityRepository
    ){
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        //AUTHORITIES
        var userAuthority = new Authority.builder()
                .name("USER")
                .build();
        
        var adminAuthority = new Authority.builder()
                .name("ADMIN")
                .build();
        
        authorityRepository.save(userAuthority);
        authorityRepository.save(adminAuthority);
        
        //USERS
        var simpleUser = new User.builder()
                .firstName("Dmitry")
                .lastName("Voicu")
                .email("069759876dv@gmail.com")
                .login("user")
                .password(passwordEncoder.encode("123"))
                .activated(true)
                .build();
        
        var adminUser = new User.builder()
                .firstName("Nick")
                .lastName("Simpson")
                .email("voiku173dmitry@gmail.com")
                .login("admin")
                .password(passwordEncoder.encode("123"))
                .activated(true)
                .build();
        
        //USERS - AUTHORITIES
        simpleUser.getAuthorities().add(userAuthority);
        adminUser.getAuthorities().add(adminAuthority);
        
        authenticationService.register(simpleUser);
        authenticationService.register(adminUser);
    }
}
