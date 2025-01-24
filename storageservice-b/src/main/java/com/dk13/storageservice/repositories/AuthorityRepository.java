package com.dk13.storageservice.repositories;

import com.dk13.storageservice.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
