package com.dk13.storageservice.responses;

import com.dk13.storageservice.entities.StorageFile;

public class StorageFileInfo {
    private String fileName;
    private Long id;
    
    public StorageFileInfo(StorageFile file) {
        this.fileName = file.getName();
        this.id =file.getId();
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public Long getId() {
        return id;
    }
}
