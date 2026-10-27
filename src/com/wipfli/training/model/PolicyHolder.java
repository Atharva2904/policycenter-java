package com.wipfli.training.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "policy_holder")
public class PolicyHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 64)
    private String userID;


    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false)
    private int age;




    public PolicyHolder(String firstName, String lastName, int age, State state) {
        this.firstName = validateName(firstName);
        this.lastName = validateName(lastName);
        this.age = validateAge(age);
        this.state = state;
    }

    // No-arg constructor required by JPA/Hibernate
    protected PolicyHolder() {
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
        if(name == null || name.trim().isBlank()){
            throw new IllegalArgumentException(String.format("Name cannot be NULL! Entered value: %s", name));
        }

        if (!name.matches("[A-Za-z]+")) {
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
