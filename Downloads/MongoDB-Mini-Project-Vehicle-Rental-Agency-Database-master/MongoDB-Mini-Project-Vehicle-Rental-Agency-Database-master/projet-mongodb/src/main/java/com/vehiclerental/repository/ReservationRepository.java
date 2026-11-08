package com.vehiclerental.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vehiclerental.entities.Reservation;

public interface ReservationRepository extends MongoRepository<Reservation, String> {

    List<Reservation> findByStatus(String status);

    List<Reservation> findByNumberOfTravelers(int count);
}
