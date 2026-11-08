package com.vehiclerental.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vehiclerental.entities.Client;
import com.vehiclerental.repository.ClientRepository;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientRepository repository;

    public ClientController(ClientRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Client> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Client getById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping
    public Client insert(@RequestBody Client client) {
        return repository.insert(client);
    }

    @PostMapping
    public Client update(@RequestBody Client client) {
        return repository.save(client);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}
