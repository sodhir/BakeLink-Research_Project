package com.example.bakelink.customers.modal;

public class CustomerProfile {
    private String fullName;
    private String address;
    private String dob;
    //private String profileImageUrl;

    // Default constructor (required for Firebase)
    public CustomerProfile() {}

    public CustomerProfile(String fullName, String address, String dob) {
        this.fullName = fullName;
        this.address = address;
        this.dob = dob;
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

}

