package com.dk13.storageservice.repositories;

import com.dk13.storageservice.entities.Authority;
import com.dk13.storageservice.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByLogin(String login);
    
    //@EntityGraph(attributePaths  = {"authorities"})
    @EntityGraph(value = "u-authorities")
    Optional<User> findFirstByLogin(String login);
    
    @Query("SELECT u.authorities FROM User u WHERE u.login = :login")
    List<Authority> findAuthoritiesByUserLogin(@Param("login") String login);
    
    Optional<User> findByActivationKey(String key);
}
