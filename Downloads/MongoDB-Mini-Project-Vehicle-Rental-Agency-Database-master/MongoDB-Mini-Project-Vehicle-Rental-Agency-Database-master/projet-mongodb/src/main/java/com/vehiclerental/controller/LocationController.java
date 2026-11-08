package com.vehiclerental.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vehiclerental.entities.Location;
import com.vehiclerental.repository.LocationRepository;

@RestController
@RequestMapping("/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationRepository repository;

    public LocationController(LocationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Location> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Location getById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping
    public Location insert(@RequestBody Location location) {
        return repository.insert(location);
    }

    @PostMapping
    public Location update(@RequestBody Location location) {
        return repository.save(location);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }

    @GetMapping("/search/{name}")
    public List<Location> searchByName(@PathVariable String name) {
        return repository.findByNameContaining(name);
    }
}
