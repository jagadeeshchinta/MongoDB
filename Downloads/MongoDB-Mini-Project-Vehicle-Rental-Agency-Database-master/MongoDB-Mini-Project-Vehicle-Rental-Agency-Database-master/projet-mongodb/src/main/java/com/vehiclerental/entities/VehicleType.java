package com.vehiclerental.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicle_types")
public class VehicleType {

    @Id
    private String id;
    private String name;
    private double rate;

    public VehicleType() {
    }

    public VehicleType(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
