package com.wipfli.training.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an abstract insurance policy within the management system.
 * <p>
 * This class serves as the central blueprint for specific vehicle policy types,
 * such as {@link BikePolicy}, {@link TruckPolicy}, and {@link CarPolicy}. It manages
 * shared attributes including PolicyHolder details, number of previous claims, vehicle type etc.
 */

public abstract class Policy {
    private final String policyNumber;
    private PolicyHolder policyHolder;

    private final Instant createdAt;
    private PolicyStatus policyStatus;

    private LocalDate startDate;

    private VehicleType vehicleType;
    private int claimsCount;


    /**
     * This constructor accepts following parameters:
     * @param policyNumber
     * @param policyHolder
     * @param vehicleType
     * It does not accept claims count as a parameter. It is set to 0 by default.
     */

    public Policy(String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType) {
        this(policyNumber, policyHolder, vehicleType, 0);
    }

    /**
     * This is overloaded constructor of Policy class which also accepts previousClaimsCount as parameter.
     * @param policyNumber
     * @param policyHolder
     * @param vehicleType
     * @param previousClaimsCount
     */

    public Policy(String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType, int previousClaimsCount) {
        this.policyNumber = policyNumber;
        this.policyHolder = policyHolder;
        this.vehicleType = vehicleType;
        this.claimsCount = previousClaimsCount;
        this.createdAt = Instant.now();
        this.startDate = LocalDate.now();
        this.policyStatus = PolicyStatus.ACTIVE;
    }



    public int getClaimsCount() {
        return claimsCount;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public PolicyStatus getPolicyStatus() {
        return policyStatus;
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    /**
     * This method checks if the policy status can be set to EXPIRED.
     * A policy status can only be set to EXPIRED if it is in ACTIVE state
     * This is done using the validateTransition function.
     */

    public void expire() {

        validateTransition(PolicyStatus.EXPIRED);
        this.policyStatus = PolicyStatus.EXPIRED;
        System.out.println(String.format("Policy Number: %s ---> \tStatus: %s", this.policyNumber, this.policyStatus));
    }

    /**
     * This method checks if the policy status can be set to RENEWED.
     * A policy status can only be set to RENEWED if it is in ACTIVE state
     * This is done using the validateTransition function.
     */
    public void renew() {
        validateTransition(PolicyStatus.RENEWED);
        this.policyStatus = PolicyStatus.RENEWED;
        System.out.println(String.format("Policy Number: %s ---> \tStatus: %s", this.policyNumber, this.policyStatus));
    }

    /**
     * This method increases the claims count exactly by 1
     */

    public void recordClaim() {
        this.claimsCount++;
    }


    /**
     * This function helps in validating the next possible transition for status of the policy
     * It uses the PolicyStatus Enum's method to check whether the given nextState is a valid one
     * @param nextStatus
     * @throws IllegalArgumentException If invalid policy state transition is encountered, this exception is thrown.
     */

    private void validateTransition(PolicyStatus nextStatus) {
        if (!this.policyStatus.canTransitionTo(nextStatus))
            throw new IllegalStateException(String.format("Business rule violation! Cannot transition policy from %s to %s.\nFor a policy to be EXPIRED or RENEWED, it must be ACTIVE!", this.policyStatus, nextStatus));
    }


    /**
     * This function overrides the default equals() method of the Object class of the Java
     * <p>The default implementation of equals() method compares the objects based on the equality of the content, not just their location in the memory.
     * In case of Policy objects, two policies with identical policy numbers should not be deemed equal if the other fields are different in those objects.
     * Which is why, the implementation is modified to explicitly compare on the basis of Policy Number only.
     * </p>
     * @param obj   the reference object with which to compare.
     * @return a boolean value
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Policy policy)) return false;

        return policyNumber.equals(policy.policyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(policyNumber);
    }


    public abstract String getPolicyDetails();



}
