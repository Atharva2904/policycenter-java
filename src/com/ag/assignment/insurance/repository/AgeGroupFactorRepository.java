package com.ag.assignment.insurance.repository;

import com.ag.assignment.insurance.model.AgeGroupFactor;

import java.util.List;

public class AgeGroupFactorRepository {
    private final List<AgeGroupFactor> ageGroupFactorList = List.of(
            new AgeGroupFactor(18, 25, 1.3),
            new AgeGroupFactor(26, 60, 1.0),
            new AgeGroupFactor(61, 100, 1.4)
    );

    public double getFactor(int age){
        for(AgeGroupFactor ageGroupFactor: ageGroupFactorList){
            if(ageGroupFactor.contains(age)){
                return ageGroupFactor.getFactor();
            }
        }

        throw new IllegalArgumentException("No such age group is defined!");
    }
}
