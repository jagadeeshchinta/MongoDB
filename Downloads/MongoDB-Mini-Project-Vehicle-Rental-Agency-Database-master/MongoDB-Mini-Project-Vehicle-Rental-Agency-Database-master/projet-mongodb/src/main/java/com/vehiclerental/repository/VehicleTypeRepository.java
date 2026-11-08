package com.vehiclerental.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vehiclerental.entities.VehicleType;

public interface VehicleTypeRepository extends MongoRepository<VehicleType, String> {

    VehicleType findByName(String name);

    List<VehicleType> findByRateLessThan(double rate);

    List<VehicleType> findByRateBetween(double min, double max);
}
