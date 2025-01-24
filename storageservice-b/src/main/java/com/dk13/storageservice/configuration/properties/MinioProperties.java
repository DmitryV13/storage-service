package com.dk13.storageservice.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio", ignoreUnknownFields = false)
public class MinioProperties {
    private String bucket;
    private String url;
    private String accessKey;
    private String secretKey;
    
    public String getBucket() {
        return bucket;
    }
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public String getAccessKey() {
        return accessKey;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
