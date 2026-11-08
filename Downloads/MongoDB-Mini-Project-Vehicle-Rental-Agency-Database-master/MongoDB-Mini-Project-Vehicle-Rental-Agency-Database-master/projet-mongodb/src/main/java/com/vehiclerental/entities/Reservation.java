package com.vehiclerental.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;
    private Date arrivalDate;
    private String status;           // accepted, pending, refused
    private String destination;
    private Date arrivalTime;
    private String comment;
    private int numberOfTravelers;
    private List<String> vehicleIds; // foreign key references

    public Reservation() {
    }

    public Reservation(Date arrivalDate, String status, String destination, Date arrivalTime,
                        String comment, int numberOfTravelers, List<String> vehicleIds) {
        this.arrivalDate = arrivalDate;
        this.status = status;
        this.destination = destination;
        this.arrivalTime = arrivalTime;
        this.comment = comment;
        this.numberOfTravelers = numberOfTravelers;
        this.vehicleIds = vehicleIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getNumberOfTravelers() {
        return numberOfTravelers;
    }

    public void setNumberOfTravelers(int numberOfTravelers) {
        this.numberOfTravelers = numberOfTravelers;
    }

    public List<String> getVehicleIds() {
        return vehicleIds;
    }

    public void setVehicleIds(List<String> vehicleIds) {
        this.vehicleIds = vehicleIds;
    }
}
