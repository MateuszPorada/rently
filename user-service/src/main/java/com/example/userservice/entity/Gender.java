package com.example.userservice.entity;

public enum Gender {
    MALE("male"),
    FEMALE("female");
    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
