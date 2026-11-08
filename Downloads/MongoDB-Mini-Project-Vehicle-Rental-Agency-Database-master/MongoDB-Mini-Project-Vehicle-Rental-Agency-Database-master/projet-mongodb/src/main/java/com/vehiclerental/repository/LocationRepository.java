package com.vehiclerental.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vehiclerental.entities.Location;

public interface LocationRepository extends MongoRepository<Location, String> {

    Location findByName(String name);

    List<Location> findByNameContaining(String name);
}
