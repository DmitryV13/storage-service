package com.dk13.storageservice.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class StorageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "VARCHAR(255)")
    private String name;
    
    private Long size;
    
    @Column(columnDefinition = "VARCHAR(255)")
    private String mimeType;
    
    @Column(columnDefinition = "VARCHAR(255)")
    private String path;
    
    private Date creationDate;
    
    @ManyToOne
    @JoinColumn(
            name = "createdBy",
            foreignKey = @ForeignKey(name = "fk_user_login"),
            referencedColumnName = "login"
    )
    private User user;
    
    public StorageFile(){}
    
    private StorageFile(builder builder){
        this.name = builder.name;
        this.size = builder.size;
        this.mimeType = builder.mimeType;
        this.path = builder.path;
        this.creationDate = builder.creationDate;
        this.user = builder.user;
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
    
    public Long getId() {
        return id;
    }
    
    public static class builder{
        private String name;
        private Long size;
        private String mimeType;
        private String path;
        private Date creationDate;
        private User user;
        
        public builder name(String name){
            this.name = name;
            return this;
        }
        
        public builder size(Long size){
            this.size = size;
            return this;
        }
        
        public builder mimeType(String mimeType){
            this.mimeType = mimeType;
            return this;
        }
        
        public builder path(String path){
            this.path = path;
            return this;
        }
        
        public builder creationDate(Date creationDate){
            this.creationDate = creationDate;
            return this;
        }
        
        public builder user(User user){
            this.user = user;
            return this;
        }
        
        public StorageFile build(){
            return new StorageFile(this);
        }
    }
}
