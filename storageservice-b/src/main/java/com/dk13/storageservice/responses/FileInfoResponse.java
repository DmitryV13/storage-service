package com.dk13.storageservice.responses;

import java.util.Date;

public class FileInfoResponse {
    private String name;
    private Long size;
    private String mimeType;
    private String path;
    private Date creationDate;
    
    public FileInfoResponse() {
    }
    
    private FileInfoResponse(Builder builder) {
        this.name = builder.name;
        this.size = builder.size;
        this.mimeType = builder.mimeType;
        this.path = builder.path;
        this.creationDate = builder.creationDate;
    }
    
    public String getName() {
        return name;
    }
    
    public Long getSize() {
        return size;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public String getPath() {
        return path;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public static class Builder{
        private String name;
        private Long size;
        private String mimeType;
        private String path;
        private Date creationDate;
        
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder setSize(Long size) {
            this.size = size;
            return this;
        }
        
        public Builder setMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }
        
        public Builder setPath(String path) {
            this.path = path;
            return this;
        }
        
        public Builder setCreationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }
        
        public FileInfoResponse build(){
            return new FileInfoResponse(this);
        }
    }
}
