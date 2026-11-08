package com.vehiclerental.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vehiclerental.entities.Vehicle;
import com.vehiclerental.repository.VehicleRepository;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    private final VehicleRepository repository;

    public VehicleController(VehicleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Vehicle> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping
    public Vehicle insert(@RequestBody Vehicle vehicle) {
        return repository.insert(vehicle);
    }

    @PostMapping
    public Vehicle update(@RequestBody Vehicle vehicle) {
        return repository.save(vehicle);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }

    @GetMapping("/seats/{min}/{max}")
    public List<Vehicle> getBySeats(@PathVariable int min, @PathVariable int max) {
        return repository.findByNumberOfSeatsBetween(min, max);
    }
}
