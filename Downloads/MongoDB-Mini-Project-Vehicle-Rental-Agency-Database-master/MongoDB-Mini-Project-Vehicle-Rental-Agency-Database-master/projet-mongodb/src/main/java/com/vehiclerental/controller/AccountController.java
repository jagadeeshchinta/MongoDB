package com.vehiclerental.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vehiclerental.entities.Account;
import com.vehiclerental.repository.AccountRepository;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountRepository repository;

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Account> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Account getById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping
    public Account insert(@RequestBody Account account) {
        return repository.insert(account);
    }

    @PostMapping
    public Account update(@RequestBody Account account) {
        return repository.save(account);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }

    @GetMapping("/login/{login}")
    public Account getByLogin(@PathVariable String login) {
        return repository.findByLogin(login);
    }
}
