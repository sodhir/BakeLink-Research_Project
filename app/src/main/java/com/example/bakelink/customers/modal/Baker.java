package com.example.bakelink.customers.modal;

import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;

import java.util.List;

public class Baker {

    String id;
    private String name;
    private String description;
    private String specialities;
    private String imageUrl;
    private float rating;

    private String bakeryTitle,profilePictureUrl;
    private List<String> specialtiesAndServices;

    public Baker() {
    }

    // Constructor
    public Baker(String name, String imageUrl, float rating) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public Baker(String id, String name, String description, String specialities, String imageUrl, float rating, String bakeryTitle, List<String> specialtiesAndServices, String profilePictureUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.specialities = specialities;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.bakeryTitle = bakeryTitle;
        this.specialtiesAndServices = specialtiesAndServices;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String specialities) {
        this.specialities = specialities;
    }

    public String getBakeryTitle() {
        return bakeryTitle;
    }

    public void setBakeryTitle(String bakeryTitle) {
        this.bakeryTitle = bakeryTitle;
    }

    public List<String> getSpecialtiesAndServices() {
        return specialtiesAndServices;
    }

    public void setSpecialtiesAndServices(List<String> specialtiesAndServices) {
        this.specialtiesAndServices = specialtiesAndServices;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "Baker{" +
                "name='" + bakeryTitle + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + profilePictureUrl + '\'' +
                ", specialities='" + specialtiesAndServices + '\'' +
                '}';
    }
}
