package com.vehiclerental.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vehiclerental.entities.Client;

public interface ClientRepository extends MongoRepository<Client, String> {
}
