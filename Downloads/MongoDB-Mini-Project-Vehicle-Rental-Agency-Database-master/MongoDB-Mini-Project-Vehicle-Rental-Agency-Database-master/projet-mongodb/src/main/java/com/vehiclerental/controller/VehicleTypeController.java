package com.vehiclerental.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vehiclerental.entities.VehicleType;
import com.vehiclerental.repository.VehicleTypeRepository;

@RestController
@RequestMapping("/vehicle-types")
@CrossOrigin(origins = "*")
public class VehicleTypeController {

    private final VehicleTypeRepository repository;

    public VehicleTypeController(VehicleTypeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<VehicleType> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public VehicleType getById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping
    public VehicleType insert(@RequestBody VehicleType vehicleType) {
        return repository.insert(vehicleType);
    }

    @PostMapping
    public VehicleType update(@RequestBody VehicleType vehicleType) {
        return repository.save(vehicleType);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }

    @GetMapping("/rate/{max}")
    public List<VehicleType> getByRate(@PathVariable double max) {
        return repository.findByRateLessThan(max);
    }

    @GetMapping("/rate/{min}/{max}")
    public List<VehicleType> getByRateRange(@PathVariable double min, @PathVariable double max) {
        return repository.findByRateBetween(min, max);
    }
}
