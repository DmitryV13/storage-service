package com.dk13.storageservice.responses;

import java.io.InputStream;

public class DownloadFileResponse {
    private String mimeType;
    private InputStream inputStream;
    private String fileName;
    
    public DownloadFileResponse(String fileName, String mimeType, InputStream inputStream) {
        this.mimeType = mimeType;
        this.fileName = fileName;
        this.inputStream = inputStream;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }
    
    public String getFileName() {
        return fileName;
    }
}
