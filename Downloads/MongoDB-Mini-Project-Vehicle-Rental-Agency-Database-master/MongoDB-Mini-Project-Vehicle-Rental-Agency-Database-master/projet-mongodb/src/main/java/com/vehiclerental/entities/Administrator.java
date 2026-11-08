package com.vehiclerental.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "administrators")
public class Administrator {

    @Id
    private String id;
    private String name;
    private String address;
    private String email;
    private String type;
    private String accountId; // foreign key reference

    public Administrator() {
    }

    public Administrator(String name, String address, String email, String type, String accountId) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.type = type;
        this.accountId = accountId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
