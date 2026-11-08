package com.vehiclerental.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicles")
public class Vehicle {

    @Id
    private String id;
    private String brand;
    private String power;
    private int numberOfSeats;
    private String vehicleTypeId; // foreign key reference
    private String locationId;    // foreign key reference

    public Vehicle() {
    }

    public Vehicle(String brand, String power, int numberOfSeats, String vehicleTypeId, String locationId) {
        this.brand = brand;
        this.power = power;
        this.numberOfSeats = numberOfSeats;
        this.vehicleTypeId = vehicleTypeId;
        this.locationId = locationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
