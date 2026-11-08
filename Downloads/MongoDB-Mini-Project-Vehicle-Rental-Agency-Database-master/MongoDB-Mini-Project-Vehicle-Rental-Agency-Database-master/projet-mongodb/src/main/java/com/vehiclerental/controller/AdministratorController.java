package com.vehiclerental.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vehiclerental.entities.Administrator;
import com.vehiclerental.repository.AdministratorRepository;

@RestController
@RequestMapping("/administrators")
@CrossOrigin(origins = "*")
public class AdministratorController {

    private final AdministratorRepository repository;

    public AdministratorController(AdministratorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Administrator> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Administrator getById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping
    public Administrator insert(@RequestBody Administrator administrator) {
        return repository.insert(administrator);
    }

    @PostMapping
    public Administrator update(@RequestBody Administrator administrator) {
        return repository.save(administrator);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }
}
