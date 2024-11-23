package com.example.flowerobjectdetection;

import java.util.List;
public class UserProfileData {

    private List<String> savedPlants;

    public UserProfileData() {}

    public UserProfileData(List<String> savedPlants) {
        this.savedPlants = savedPlants;
    }

    public List<String> getSavedPlants() {
        return savedPlants;
    }

    public void setSavedPlants(List<String> savedPlants) {
        this.savedPlants = savedPlants;
    }
}
