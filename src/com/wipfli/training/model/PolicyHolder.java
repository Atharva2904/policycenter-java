package com.wipfli.training.model;

import java.util.UUID;

public class PolicyHolder {
    private final String userID;
    private String firstName;
    private String lastName;

    private State state;
    private int age;


    public PolicyHolder(String firstName, String lastName, int age, State state) {
        this.userID = UUID.randomUUID().toString();
        this.firstName = validateName(firstName);
        this.lastName = validateName(lastName);
        this.age = validateAge(age);
        this.state = state;
    }


    public long getAge() {
        return age;
    }

    public String getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    private static String validateName(String name) {
        if (name == null || !name.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException(String.format("Name cannot contain digits or special character! Entered value: %s", name));
        }

        return name;
    }

    private static int validateAge(int age) {
        if (age <= 0) throw new IllegalArgumentException("Age must be a value greater than 0!");


        if (age < 18)
            throw new IllegalArgumentException("Policyholder must be at least 18 years old to hold a policy.");


        if (age > 100) throw new IllegalArgumentException("Age cannot be more than 100!!");

        return age;
    }
}
