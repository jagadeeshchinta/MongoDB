package com.vehiclerental.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vehiclerental.entities.Administrator;

public interface AdministratorRepository extends MongoRepository<Administrator, String> {
}
