package com.wipfli.training.service;

import com.wipfli.training.model.*;

import java.time.LocalDate;

public class PolicyFactory {

    /*
        This is a factory class which is used to create different types of policies.
        It has three static factory methods defined which are responsible for creation and supply of new Policy objects.
        Instead of scattering object creation calls across the main application, these factory methods serve as single point for the same.
        It also helps in decoupling the object creation logic from the main application logic. The main application now just receives input and provides it to these factory methods,
        which in turn 'produce' new objects (similar analogy to products being developed inside any factory) and supplies them back to the main app.

     */

    public static Policy createCarPolicy(String policyNumber, PolicyHolder policyHolder, String registrationNumber, int numberOfClaims, LocalDate expiryDate){
        CarPolicy carPolicy = new CarPolicy(policyNumber, policyHolder, registrationNumber, numberOfClaims,  expiryDate);
        return carPolicy;
    }
    public static Policy createBikePolicy(String policyNumber, PolicyHolder policyHolder,
                                        int engineCapacity,int numberOfClaims,  LocalDate expiryDate){
        return new BikePolicy(policyNumber, policyHolder, engineCapacity, expiryDate);

    }
    public static Policy createTruckPolicy(String policyNumber, PolicyHolder policyHolder
                        , double loadCapacity, int numberOfClaims, LocalDate expiryDate) {
        return new TruckPolicy(policyNumber, policyHolder, loadCapacity, expiryDate);

    }
}
