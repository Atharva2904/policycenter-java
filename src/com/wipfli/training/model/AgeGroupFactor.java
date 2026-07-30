package com.wipfli.training.model;

public class AgeGroupFactor {
    private final int minAge;
    private final int maxAge;
    private final double factor;

    public AgeGroupFactor(int minAge, int maxAge, double factor){
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.factor = factor;
    }

    public boolean contains(int age){
        return age >= minAge && age <= maxAge;
    }

    public double getFactor() {
        return factor;
    }
}

