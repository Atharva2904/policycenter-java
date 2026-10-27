package com.wipfli.training.model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("BIKE")

public class BikePolicy extends Policy {

    @Column(name = "engine_cc",
            check = @CheckConstraint(constraint = "engine_cc > 0", name = "engine_cc_positive")
    )
    private int engineCC;

    public BikePolicy(String policyNumber, PolicyHolder policyHolder, int engineCC, LocalDate expiryDate){
        this(policyNumber, policyHolder, engineCC, 0, expiryDate);
    }

    public BikePolicy(String policyNumber, PolicyHolder policyHolder, int engineCC, int previousClaimsCount, LocalDate expiryDate){
        super(policyNumber, policyHolder, VehicleType.BIKE, previousClaimsCount, expiryDate);
        this.engineCC = engineCC;
    }

    // No-arg constructor required by JPA/Hibernate
    protected BikePolicy() {
        super();
    }

    public int getEngineCC() {
        return engineCC;
    }

    @Override
    public String getPolicyDetails() {
        return getCommonDetails() + String.format("""
            Vehicle Type:        %s
            Engine CC:           %s
            ======================================
            """,
                VehicleType.BIKE,
                getEngineCC()
        );
    }
}
