package com.vehiclerental.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vehiclerental.entities.Reservation;
import com.vehiclerental.repository.ReservationRepository;

@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationRepository repository;

    public ReservationController(ReservationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Reservation> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Reservation getById(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping
    public Reservation insert(@RequestBody Reservation reservation) {
        return repository.insert(reservation);
    }

    @PostMapping
    public Reservation update(@RequestBody Reservation reservation) {
        return repository.save(reservation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        repository.deleteById(id);
    }

    @GetMapping("/status/{status}")
    public List<Reservation> getByStatus(@PathVariable String status) {
        return repository.findByStatus(status);
    }
}
