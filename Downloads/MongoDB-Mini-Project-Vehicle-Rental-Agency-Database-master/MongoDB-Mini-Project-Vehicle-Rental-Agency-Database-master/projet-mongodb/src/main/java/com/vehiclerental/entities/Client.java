package com.vehiclerental.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
public class Client {

    @Id
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private List<String> reservationIds; // foreign key references

    public Client() {
    }

    public Client(String name, String address, String phone, String email, List<String> reservationIds) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.reservationIds = reservationIds;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getReservationIds() {
        return reservationIds;
    }

    public void setReservationIds(List<String> reservationIds) {
        this.reservationIds = reservationIds;
    }
}
