package com.wipfli.training.model;

/*
    This interface defines a contract for calculating the premium of an insurance policy.
    Any class that implements this interface must provide an implementation for the calculatePremium method,
    which takes a Policy object as input and returns the calculated premium as a Double.

    It is an example of Strategy Pattern in action, where different calculators implemented using this interface can have related capabilities.
    It is dynamically decided during the program run which shape will be taken by an object of the type of this interface.
    It defines a contract for family of interchangeable algorithms (e.g., StandardPremiumCalculator, NoClaimBonusCalculator etc. in this context).

    There could be different strategies to tackle a problem. These are decided by analysing the nature of the task at hand and the desired result.
    The same principle is being applied here as well.
 */



public interface PremiumCalculable {

    Double calculatePremium(Policy policy);

}
