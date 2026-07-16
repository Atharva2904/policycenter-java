package com.ag.assignment.insurance.app;

import com.ag.assignment.insurance.model.*;
import com.ag.assignment.insurance.repository.AgeGroupFactorStore;
import com.ag.assignment.insurance.service.NoClaimBonusCalculator;
import com.ag.assignment.insurance.service.PolicyValidator;
import com.ag.assignment.insurance.service.StandardPremiumCalculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PremiumCalculator {
    public static void main(String[] args) {
        try (Scanner detailsScanner = new Scanner(System.in)) {
            AgeGroupFactorStore ageGroupFactorStore = new AgeGroupFactorStore();

            System.out.println("--------------Welcome to Policy Management system!--------------");
            System.out.print("Enter number of policies >> ");
            int numPolicies = detailsScanner.nextInt();
            int count = 0;
            detailsScanner.nextLine();

            List<Policy> policyArray = new ArrayList<>();

            while (numPolicies > 0) {
                System.out.println("=======================================================");
                System.out.println("Enter details for Policy " + ++count + " >> ");
                System.out.println("=======================================================");
                boolean isValid = false;

                while (!isValid) {
                    try {
                        PolicyHolder policyHolder = addNewPolicyHolder(detailsScanner);

                        System.out.println("Select Vehicle Type (Enter respective number):");
                        System.out.print("1. CAR\t2. TRUCK\t3. BIKE >> ");
                        VehicleType vehicleType = VehicleType.getVehicleFromChoice(Integer.parseInt(detailsScanner.nextLine()));


                        System.out.print("Do you have any history of previous claims? If yes, enter number of claims >> ");
                        int numberOfClaims = detailsScanner.nextInt();
                        detailsScanner.nextLine();

                        System.out.print("Enter policy number >> ");
                        String policyNumber = detailsScanner.nextLine();

                        Policy policy = new Policy(policyNumber, policyHolder, vehicleType, numberOfClaims);
                        if (PolicyValidator.validate(policy)) {
                            numPolicies--;
                            policyArray.add(policy);
                            isValid = true;
                            System.out.println("Policy validated successfully!!");
                        }
                        else{
                            System.out.println("Policy Validation failed! Please re-enter details for this policy.");
                        }
                    }
                    catch (Exception e){
                        System.out.println("\n[ERROR] Invalid input or processing error: " + e.getMessage());
                        System.out.println("Please restart entering details for this policy.\n");

                        if (detailsScanner.hasNextLine()) {
                            detailsScanner.nextLine();
                        }
                    }

                }
            }

            StandardPremiumCalculator standardPremiumCalculator = new StandardPremiumCalculator(ageGroupFactorStore);
            NoClaimBonusCalculator noClaimBonusCalculator = new NoClaimBonusCalculator(ageGroupFactorStore);

            for (Policy policy : policyArray) {
                BigDecimal standardPremium = standardPremiumCalculator.calculatePremium(policy);
                BigDecimal discountedPremium = noClaimBonusCalculator.calculatePremium(policy);
                System.out.println("=======================================================");

                System.out.println("Policy Number >> " + policy.getPolicyNumber());
                System.out.println("Policy Holder Name >> " + policy.getPolicyHolder().getDisplayName());
                System.out.println("Policy Holder Age >> " + policy.getPolicyHolder().getAge());
                System.out.println("Standard Premium >> " + standardPremium);
                System.out.println("Discounted Premium >> " + discountedPremium);

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static PolicyHolder addNewPolicyHolder(Scanner scn) {
        String firstName, lastName;

        System.out.print("Enter your first name >> ");
        firstName = scn.nextLine();

        System.out.print("Enter your last name >> ");
        lastName = scn.nextLine();


        System.out.print("Enter your age >> ");
        int age = scn.nextInt();
        scn.nextLine();
        PolicyValidator.validateAge(age);

        System.out.println("\nSelect your jurisdiction from the following choices >> ");
        System.out.println("1.Illinois(IS)\t2.Indiana(IN)\t3.Minnesota(MN) (Enter respective number)");
        System.out.print(">>");
        State personState = State.getStateFromChoice(Integer.parseInt(scn.nextLine()));


        PolicyHolder policyHolder = new PolicyHolder(firstName, lastName, age, personState);

        System.out.println("Account created successfully! Your userID is: " + policyHolder.getUserID());

        return policyHolder;
    }
}
