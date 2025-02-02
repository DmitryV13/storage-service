package com.dk13.storageservice.configuration;

import com.dk13.storageservice.configuration.other.SecureUserDetails;
import com.dk13.storageservice.configuration.properties.MinioProperties;
import com.dk13.storageservice.entities.User;
import com.dk13.storageservice.repositories.UserRepository;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {
    
    
    private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);
    private final UserRepository userRepository;
    private final MinioProperties minioProperties;
    
    public ApplicationConfig(UserRepository userRepository, MinioProperties minioProperties) {
        this.userRepository = userRepository;
        this.minioProperties = minioProperties;
    }
    
    @Bean
    public MinioClient minioClient() {
        String h =  minioProperties.getUrl();
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findFirstByLogin(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                return new SecureUserDetails(user);
            }
        };
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    
    //@Bean
    //public JavaMailSender mailSender() {
    //    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    //
    //    javaMailSender.setProtocol("SMTP");
    //    javaMailSender.setHost("127.0.0.1");
    //    javaMailSender.setPort(587);// 587 for STARTTLS, 465 for SSL
    //
    //    Properties props = javaMailSender.getJavaMailProperties();
    //    props.put("mail.transport.protocol", "smtp");
    //    props.put("mail.smtp.auth", "true");
    //    props.put("mail.smtp.starttls.enable", "true");
    //    props.put("mail.debug", "true");
    //
    //    return javaMailSender;
    //}
}
