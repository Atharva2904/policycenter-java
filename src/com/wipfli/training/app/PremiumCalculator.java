package com.wipfli.training.app;

import com.wipfli.training.model.*;
import com.wipfli.training.store.AgeGroupFactorStore;
import com.wipfli.training.service.NoClaimBonusCalculator;
import com.wipfli.training.service.PolicyValidator;
import com.wipfli.training.service.StandardPremiumCalculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PremiumCalculator {
    public static AgeGroupFactorStore ageGroupFactorStore = new AgeGroupFactorStore();

    public static void main(String[] args) {
        try (Scanner detailsScanner = new Scanner(System.in)) {

            System.out.println("--------------Welcome to Policy Management system!--------------");
            System.out.print("Enter number of policies >> ");
            int numPolicies = detailsScanner.nextInt();

            if(numPolicies <= 0) throw new IllegalArgumentException("Number of Policies must be greater than 0!");
            int count = 0;
            detailsScanner.nextLine();

            List<Policy> policyArray = new ArrayList<>();

            while (numPolicies > 0) {
                System.out.println("=======================================================");
                System.out.println("Enter details for Policy " + ++count + " >> ");
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


                        } else {
                            System.out.println("Policy Validation failed! Please re-enter details for this policy.");
                        }
                    } catch (Exception e) {
                        System.out.println("\n[ERROR] Invalid input or processing error: " + e.getMessage());
                        System.out.println("Please restart entering details for this policy.\n");

                        if (detailsScanner.hasNextLine()) {
                            detailsScanner.nextLine();
                        }
                    }

                }
            }



            for (Policy policy : policyArray) {

                // Calling the policy operations menu function
                System.out.println("=======================================================");
                System.out.println("Policy Number >> " + policy.getPolicyNumber());
                System.out.println("Policy Holder Name >> " + policy.getPolicyHolder().getDisplayName());
                System.out.println("Policy Holder Age >> " + policy.getPolicyHolder().getAge());
                System.out.println("Previous Claims Count >> " + policy.getClaimsCount());
                System.out.println("Policy Status >> " + policy.getPolicyStatus());
                System.out.println("=======================================================");

                simulatePolicyOperations(policy, detailsScanner);



            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void simulatePolicyOperations(Policy policy, Scanner detailsScanner) {
        try {
            PremiumCalculable standardPremiumCalculator = new StandardPremiumCalculator(ageGroupFactorStore);
            PremiumCalculable noClaimBonusCalculator = new NoClaimBonusCalculator(ageGroupFactorStore);

            boolean exit = false;
            while (!exit) {
                System.out.println("\n================ POLICY OPERATIONS MENU ================");
                System.out.println("1. Compute / View Premium");
                System.out.println("2. Record a Claim (Increment Claims)");
                System.out.println("3. Expire Policy");
                System.out.println("4. Renew Policy");
                System.out.println("5. Exit");
                System.out.print("Enter choice >> ");

                int choice = Integer.parseInt(detailsScanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.println("\n[ACTION] Computing Premium...");
                        BigDecimal standardPremium = standardPremiumCalculator.calculatePremium(policy);
                        BigDecimal discountedPremium = noClaimBonusCalculator.calculatePremium(policy);
                        System.out.println("Standard Premium >> " + standardPremium);
                        System.out.println("Discounted Premium >> " + discountedPremium);
                        break;

                    case 2:
                        policy.recordClaim();
                        System.out.println("\n[ACTION] recordClaim() called - claims is now " + policy.getClaimsCount());
                        break;

                    case 3:
                        policy.expire();
                        break;

                    case 4:
                        policy.renew();
                        break;

                    case 5:
                        System.out.println("\nExiting System. Goodbye!");
                        exit = true;
                        break;

                    default:
                        System.out.println("\n[ERROR] Invalid option. Please select 1-5.");
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println("\n[ERROR] Execution halted: " + e.getMessage());
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
