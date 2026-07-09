package com.ag.assignment.insurance.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class PolicyHolder {
    private final String userID;
    private String firstName;
    private String middleName;
    private String lastName;

    private LocalDate dateOfBirth;
    private State state;

    private int age;

    private int previousClaims;
    private int numberOfCrashes;

    public PolicyHolder() {
        this.userID = UUID.randomUUID().toString();
    }

    public PolicyHolder(String firstName, String lastName, int age, State state){
        this();
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
        setState(state);
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

    public void setNumberOfCrashes(int numberOfCrashes) {
        if (numberOfCrashes < 0) {
            throw new IllegalArgumentException("Number of crashes cannot be negative.");
        }

        this.numberOfCrashes = numberOfCrashes;
    }

    public void setAge(int age) {
        if(age <= 0) throw new IllegalArgumentException("Age must be a value greater than 0!");

        // Checks for legal age of user registering for the policy
        if(age < 18) throw new IllegalArgumentException("Policyholder must be at least 18 years old to hold a policy.");

        this.age = age;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || !firstName.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("First name cannot contain digits or special character!");
        }

        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        if (middleName == null || !middleName.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Middle name cannot contain digits or special character!");
        }

        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || !lastName.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("Last name cannot contain digits or special character!");
        }

        this.lastName = lastName;
    }

    public void setDateOfBirth(String dateOfBirth) {

        // Using DateTimeFormatter class of Java to parse the date string into LocalDate object
        // Since, the input is a string, DateTimeFormatter makes it easier to parse it into required
        // format of the date

        String dateFormat = "dd/MM/uuuu";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withResolverStyle(ResolverStyle.STRICT);
        LocalDate dob = LocalDate.parse(dateOfBirth, formatter);

        if (dob.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be from the future!\n");
        }
        this.dateOfBirth = dob;
    }

    public void setState(State state) {
        this.state = state;
    }

    protected void setPreviousClaims(int previousClaims) {
        this.previousClaims = previousClaims;
    }

    public int getNumberOfCrashes() {
        return numberOfCrashes;
    }

    public int getPreviousClaims() {
        return previousClaims;
    }

    public String getDisplayName() {
        return firstName + " " + middleName + " " + lastName;
    }
}
