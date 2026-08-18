package com.wipfli.training.store;

import com.wipfli.training.model.AgeGroupFactor;

import java.util.List;

public class AgeGroupFactorStore {
    private final List<AgeGroupFactor> ageGroupFactorList = List.of(
            new AgeGroupFactor(18, 25, 0.2),
            new AgeGroupFactor(26, 60, 0.0),
            new AgeGroupFactor(61, 100, 1.3)
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
