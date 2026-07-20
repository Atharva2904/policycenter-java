package com.wipfli.training.model;

import java.util.UUID;

public class PolicyHolder {
    private final String userID;
    private String firstName;
    private String lastName;

    private State state;

    private int age;

    public PolicyHolder() {
        this.userID = UUID.randomUUID().toString();
    }

    public PolicyHolder(String firstName, String lastName, int age, State state){
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.state = state;
    }


    public State getState() {
        return state;
    }

    public long getAge() {
        // Returns user's age in years

        // ===================================================
        // Had followed the date based age calculation approach earlier

        // For now just directly returns the age using the attribute value

        // LocalDate todayDate = LocalDate.now();
        // return dateOfBirth.until(todayDate, ChronoUnit.YEARS);
        // return ChronoUnit.YEARS.between(dateOfBirth, todayDate);
        // ===================================================

        return age;
    }


    public String getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return firstName + " " + lastName;
    }
}
