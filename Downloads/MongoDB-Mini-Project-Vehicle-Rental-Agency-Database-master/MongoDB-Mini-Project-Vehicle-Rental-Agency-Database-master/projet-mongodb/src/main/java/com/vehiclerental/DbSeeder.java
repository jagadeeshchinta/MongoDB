package com.vehiclerental;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.vehiclerental.entities.*;
import com.vehiclerental.repository.*;

/**
 * Seeds the MongoDB database with sample data on application startup.
 * Clears existing data and inserts fresh sample records for testing.
 */
@Component
public class DbSeeder implements CommandLineRunner {

    private final AdministratorRepository administratorRepository;
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final LocationRepository locationRepository;
    private final ReservationRepository reservationRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleRepository vehicleRepository;

    public DbSeeder(
            AdministratorRepository administratorRepository,
            ClientRepository clientRepository,
            AccountRepository accountRepository,
            LocationRepository locationRepository,
            ReservationRepository reservationRepository,
            VehicleTypeRepository vehicleTypeRepository,
            VehicleRepository vehicleRepository) {
        this.administratorRepository = administratorRepository;
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.locationRepository = locationRepository;
        this.reservationRepository = reservationRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Drop all existing data
        vehicleTypeRepository.deleteAll();
        locationRepository.deleteAll();
        vehicleRepository.deleteAll();
        reservationRepository.deleteAll();
        clientRepository.deleteAll();
        administratorRepository.deleteAll();
        accountRepository.deleteAll();

        // --- Create Accounts ---
        Account acc1 = new Account("admin1", "pass123");
        Account acc2 = new Account("admin2", "pass456");
        Account acc3 = new Account("admin3", "pass789");
        accountRepository.saveAll(Arrays.asList(acc1, acc2, acc3));

        // --- Create Vehicle Types ---
        VehicleType sedan = new VehicleType("Sedan", 50.0);
        VehicleType suv = new VehicleType("SUV", 75.0);
        VehicleType van = new VehicleType("Van", 90.0);
        vehicleTypeRepository.saveAll(Arrays.asList(sedan, suv, van));

        // --- Create Locations ---
        Location loc1 = new Location("Downtown Office");
        Location loc2 = new Location("Airport Branch");
        Location loc3 = new Location("Mall Kiosk");
        locationRepository.saveAll(Arrays.asList(loc1, loc2, loc3));

        // --- Create Administrators ---
        // Retrieve saved accounts to get their generated IDs
        acc1 = accountRepository.findByLogin("admin1");
        acc2 = accountRepository.findByLogin("admin2");
        acc3 = accountRepository.findByLogin("admin3");

        Administrator admin1 = new Administrator("John Smith", "123 Main St", "john@rental.com", "SuperAdmin", acc1.getId());
        Administrator admin2 = new Administrator("Jane Doe", "456 Oak Ave", "jane@rental.com", "Manager", acc2.getId());
        Administrator admin3 = new Administrator("Bob Wilson", "789 Pine Rd", "bob@rental.com", "Staff", acc3.getId());
        administratorRepository.saveAll(Arrays.asList(admin1, admin2, admin3));

        // --- Create Vehicles ---
        // Retrieve saved types and locations to get their generated IDs
        sedan = vehicleTypeRepository.findByName("Sedan");
        suv = vehicleTypeRepository.findByName("SUV");
        loc1 = locationRepository.findByName("Downtown Office");
        loc2 = locationRepository.findByName("Airport Branch");

        Vehicle v1 = new Vehicle("Toyota Camry", "200HP", 5, sedan.getId(), loc1.getId());
        Vehicle v2 = new Vehicle("Honda CR-V", "190HP", 5, suv.getId(), loc2.getId());
        Vehicle v3 = new Vehicle("Ford Transit", "250HP", 8, suv.getId(), loc1.getId());
        vehicleRepository.saveAll(Arrays.asList(v1, v2, v3));

        // --- Create Reservations ---
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        // Get vehicles with more than 2 seats for the reservation
        List<Vehicle> availableVehicles = vehicleRepository.findByNumberOfSeatsGreaterThan(2);
        List<String> vehicleIds = new ArrayList<>();
        for (Vehicle v : availableVehicles) {
            vehicleIds.add(v.getId());
        }

        Reservation res1 = new Reservation(
                df.parse("15/01/2025"), "accepted", "New York", df.parse("20/01/2025"),
                "Business trip", 3, vehicleIds);
        Reservation res2 = new Reservation(
                df.parse("01/03/2025"), "pending", "Los Angeles", df.parse("05/03/2025"),
                "Family vacation", 5, Arrays.asList(v1.getId()));
        reservationRepository.saveAll(Arrays.asList(res1, res2));

        // --- Create Clients ---
        List<Reservation> savedReservations = reservationRepository.findByNumberOfTravelers(3);
        List<String> reservationIds = new ArrayList<>();
        for (Reservation r : savedReservations) {
            reservationIds.add(r.getId());
        }

        Client client1 = new Client("Alice Johnson", "321 Elm St", "555-0101", "alice@email.com", reservationIds);
        Client client2 = new Client("Charlie Brown", "654 Maple Dr", "555-0202", "charlie@email.com", 
                Arrays.asList(res2.getId()));
        clientRepository.saveAll(Arrays.asList(client1, client2));

        System.out.println("===========================================");
        System.out.println("  Database seeded successfully!");
        System.out.println("  Collections: accounts, vehicle_types,");
        System.out.println("  locations, administrators, vehicles,");
        System.out.println("  reservations, clients");
        System.out.println("===========================================");
    }
}
