package com.ag.assignment.insurance.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class PolicyHolder extends Entity {
    private final String userID;
    private String firstName;
    private String middleName;
    private String lastName;

    private LocalDate dateOfBirth;
    private State state;

    private int previousClaims;

    public PolicyHolder(){
        this.userID = UUID.randomUUID().toString();
    }

    public State getState() {
        return state;
    }

    public long getAge(){
        // Returns user's age in years

        LocalDate todayDate = LocalDate.now();

        return dateOfBirth.until(todayDate, ChronoUnit.YEARS);
        // return ChronoUnit.YEARS.between(dateOfBirth, todayDate);
    }


    public String getUserID() {
        return userID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
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

    public int getPreviousClaims() {
        return previousClaims;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + middleName + " " + lastName;
    }
}
