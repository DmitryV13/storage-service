package com.dk13.storageservice.repositories;

import com.dk13.storageservice.entities.StorageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageFileRepository extends JpaRepository<StorageFile, Long> {
    List<StorageFile> findAllByUserId(long userId);
    
    Optional<StorageFile> findById(long id);
}
