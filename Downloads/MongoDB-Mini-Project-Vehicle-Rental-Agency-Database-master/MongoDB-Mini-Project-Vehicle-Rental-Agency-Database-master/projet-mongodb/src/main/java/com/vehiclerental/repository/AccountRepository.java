package com.vehiclerental.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vehiclerental.entities.Account;

public interface AccountRepository extends MongoRepository<Account, String> {

    Account findByLogin(String login);
}
