package com.vehiclerental.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vehiclerental.entities.Vehicle;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {

    List<Vehicle> findByNumberOfSeatsGreaterThan(int min);

    List<Vehicle> findByNumberOfSeatsBetween(int min, int max);
}
