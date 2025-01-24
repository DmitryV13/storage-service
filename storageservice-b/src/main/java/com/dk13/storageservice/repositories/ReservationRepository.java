package com.dk13.storageservice.repositories;

import com.dk13.storageservice.entities.UserReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<UserReservation, Long> {
}
