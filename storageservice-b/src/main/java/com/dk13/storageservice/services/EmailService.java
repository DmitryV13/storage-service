package com.dk13.storageservice.services;

import com.dk13.storageservice.configuration.properties.MailProperties;
import com.dk13.storageservice.entities.User;
import com.dk13.storageservice.exceptions.MailException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    
    public EmailService(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }
    
    @Async
    public void sendSimpleMail(
            String to,
            String subject,
            String text
    ){
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setTo(to);
        message.setFrom(mailProperties.getMailHost());
        message.setSubject(subject);
        message.setText(text);
        
        mailSender.send(message);
    }
    
    @Async
    public void sendHtmlMail(
            String to,
            String subject,
            String htmlContent
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        
        message.setFrom(new InternetAddress(mailProperties.getMailHost()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html;charset=utf-8");
        
        mailSender.send(message);
    }
    
    @Async
    public void sendTemplateMail(
            User toUser,
            String subject,
            String content,
            String template
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            
            message.setFrom(new InternetAddress(mailProperties.getMailHost()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toUser.getEmail()));
            
            String htmlTemplate = readFile("src/main/resources/templates/" + template);
            String htmlContent = htmlTemplate.replace(
                    "${name}",
                    toUser.getFirstName() + " " + toUser.getLastName());
            htmlContent = htmlContent.replace("${content}", content);
            
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html;charset=utf-8");
            
            mailSender.send(message);
            
        }catch(MessagingException e){
            throw new MailException("Failed to send e-mail");
        }catch(IOException e){
            throw new MailException("Failed to find template for e-mail");
        }
    }
    
    private String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
