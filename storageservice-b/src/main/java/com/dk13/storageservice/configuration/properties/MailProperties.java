package com.dk13.storageservice.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mail", ignoreUnknownFields = false)
public class MailProperties {
    private String mailHost;
    
    public String getMailHost() {
        return mailHost;
    }
    
    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }
}
