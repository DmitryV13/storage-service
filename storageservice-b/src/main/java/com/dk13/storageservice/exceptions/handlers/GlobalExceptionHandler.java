package com.dk13.storageservice.exceptions.handlers;

import com.dk13.storageservice.exceptions.*;
import com.dk13.storageservice.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ErrorResponse> minioExceptionHandler(ReservationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(StorageFileException.class)
    public ResponseEntity<ErrorResponse> storageFileExceptionHandler(StorageFileException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> userExceptionHandler(UserException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(AllocationException.class)
    public ResponseEntity<ErrorResponse> allocationExceptionHandler(AllocationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponse> mailExceptionHandler(MailException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
