package com.wipfli.training.model;

public enum PolicyStatus {
    ACTIVE,
    EXPIRED,
    RENEWED;

    /**
     * This function determines the valid states to which a PolicyStatus can transition to from the given current state.
     * It returns boolean value depending on the next transition status provided.
     * It is a package-private method which is used by Policy to determine the valid transition
     * The external classes (outside to this package) need not know about the internal implementation details of the policy domain modeling.
     * Hence, it is not exposed as public API method.
     * @param nextStatus
     * @return true or false
     */
    boolean canTransitionTo(PolicyStatus nextStatus){
        return switch (this){
            case ACTIVE -> nextStatus == EXPIRED || nextStatus == RENEWED;  // Policy can only be EXPIRED/RENEWED if it is ACTIVE
            case EXPIRED -> false;      // if Policy is Expired, it can neither be RENEWED nor be made ACTIVE again
            case RENEWED -> nextStatus == ACTIVE;   // Policy can be ACTIVE only if it is in RENEWED state
        };
    }
}
