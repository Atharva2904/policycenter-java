package com.wipfli.training.model;

import com.wipfli.training.exception.IllegalStatusChangeException;
import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.RenewalNotAllowedException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
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
    private LocalDate expiryDate;

    private VehicleType vehicleType;
    private int claimsCount;


    /**
     * This constructor accepts following parameters:
     * @param policyNumber
     * @param policyHolder
     * @param vehicleType
     * It does not accept claims count as a parameter. It is set to 0 by default.
     */

    public Policy(String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType, LocalDate expiryDate) {
        this(policyNumber, policyHolder, vehicleType, 0, expiryDate);
    }

    /**
     * This is overloaded constructor of Policy class which also accepts previousClaimsCount as parameter.
     * @param policyNumber
     * @param policyHolder
     * @param vehicleType
     * @param previousClaimsCount
     */

    public Policy(String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType, int previousClaimsCount, LocalDate expiryDate) {
        this.policyNumber = policyNumber;
        this.policyHolder = policyHolder;
        this.vehicleType = vehicleType;
        this.claimsCount = previousClaimsCount;
        this.createdAt = Instant.now();
        this.startDate = LocalDate.now();
        this.expiryDate = parseAndValidate(expiryDate);
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

    public LocalDate getExpiryDate() {
        return expiryDate;
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
     * This method checks if the policy status can be set to RENEWED. Before renewal, it runs 3 checks:
     * 1. A policy status can only be set to {@code RENEWED} if it is in {@code ACTIVE} state, forcing it to throw {@link IllegalStatusChangeException}
     * 2. If Active Claims count is more than 3, the policy cannot be {@code RENEWED}.
     * 3. If renewalDate is 30 days prior to the expiry date of policy, the policy cannot be {@code RENEWED}.
     * Rules 2 and 3 force it to throw {@link RenewalNotAllowedException}
     * @param renewalDate
     */
    public void renew(LocalDate renewalDate) {
        validateTransition(PolicyStatus.RENEWED);

        // If live claims count is more than 3, then the policy cannot be RENEWED
        if(this.claimsCount > 3){
            throw new RenewalNotAllowedException(this.policyNumber,
                    String.format("You have active claims count of more than 3. (Active Claims count: %d). The policy must go to underwriting", this.claimsCount));
        }


        if(renewalDate.isAfter(this.expiryDate))
            throw new RenewalNotAllowedException(this.policyNumber,
                    String.format(
                            "Policy %s cannot be renewed on %s. Expiry date of the Policy is %s\n" +
                                    "Policy cannot be renewed after it has expired!",
                            this.policyNumber,
                            renewalDate,
                            this.expiryDate
                    ));

        // If renewalDate is more than 30 days prior to the expiry date of policy, it should throw RenewalNotAllowedException
        LocalDate earliestAllowedRenewalDate = this.expiryDate.minusDays(30);
        if(renewalDate.isBefore(earliestAllowedRenewalDate)) {
            long daysTooEarly = ChronoUnit.DAYS.between(renewalDate, earliestAllowedRenewalDate);
            throw new RenewalNotAllowedException(this.policyNumber,
                    String.format(
                            "Policy %s cannot be renewed on %s. The request is being made %d days too early.\n" +
                                    "Renewal window opens on %s (30 days prior to expiry %s).",
                            this.policyNumber,
                            renewalDate,
                            daysTooEarly,
                            earliestAllowedRenewalDate,
                            this.expiryDate
                    ));
        }


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
     * @throws IllegalStatusChangeException If invalid policy state transition is encountered, this exception is thrown.
     */

    private void validateTransition(PolicyStatus nextStatus) {
        if (!this.policyStatus.canTransitionTo(nextStatus))
            throw new IllegalStatusChangeException(this.policyNumber, String.format("Business rule violation! Cannot transition policy from %s to %s.\nFor a policy to be EXPIRED or RENEWED, it must be ACTIVE!", this.policyStatus, nextStatus));
    }

    /**
     * This is a helper function which helps in validating the expiry date provided as input.
     * The method checks for the expiry date from the past. If such input found, it throws {@link InvalidPolicyDataException}
     * @param expiryDate
     * @return {@link LocalDate expiryDate}
     */
    private LocalDate parseAndValidate(LocalDate expiryDate){

        if (expiryDate.isBefore(LocalDate.now())) {
            throw new InvalidPolicyDataException(this.policyNumber,
                    String.format("Invalid expiry date received! Expiry date must be after today's date (%s).\n", LocalDate.now())
                    );
        }

        return expiryDate;
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

    protected String getCommonDetails(){
        return String.format("""
    ======================================
    POLICY DETAILS
    ======================================
    Policy Number:       %s
    Customer Name:       %s
    Status:              %s
    Expiry Date:         %s
    Claims Count:        %d
    """,
                getPolicyNumber(),
                getPolicyHolder().getDisplayName(),
                getPolicyStatus(),
                getExpiryDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                getClaimsCount()
        );

    }



}
