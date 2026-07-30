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

            // List of Policy responsible for holding different 'Policy' objects.
            List<Policy> policyArray = new ArrayList<>();


            collectCarPolicyInput(detailsScanner, policyArray);
            collectTruckPolicyInput(detailsScanner, policyArray);
            collectBikePolicyInput(detailsScanner, policyArray);


            for (Policy policy : policyArray) {
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

    /**
     * It is a helper function which is responsible for collecting details regarding {@link CarPolicy}
     * It keeps the input loop running till valid policy details are not entered.
     * The policy details are validated through the static method {@code validate()} of {@link  PolicyValidator}
     * Upon successful validation, the policy object is added into the {@code policyArray} list.
     *
     * @param scanner
     * @param policyArray
     */

    private static void collectCarPolicyInput(Scanner scanner, List<Policy> policyArray) {
        boolean isCarValid = false;
        Policy carPolicy;
        while (!isCarValid) {
            try {

                System.out.println("\n=== Car Policy Input ===");

                PolicyHolder policyHolder = addNewPolicyHolder(scanner);

                System.out.print("Enter Policy Number: ");
                String policyNumber = scanner.nextLine();

                System.out.print("Enter Vehicle Registration Number: ");
                String registrationNumber = scanner.nextLine();

                carPolicy = new CarPolicy(
                        policyNumber,
                        policyHolder,
                        registrationNumber
                );


                if (PolicyValidator.validate(carPolicy)) {
                    policyArray.add(carPolicy);
                    isCarValid = true;
                } else {
                    System.out.println("\n[WARNING] Policy validation failed. Please try again.");
                }

            } catch (Exception e) {
                System.out.println("\n[ERROR] Invalid input or processing error: " + e.getMessage());
                System.out.println("Please restart entering details for this policy.\n");

                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
        }


    }

    /**
     * It is a helper function which is responsible for collecting details regarding {@link TruckPolicy}
     * It keeps the input loop running till valid policy details are not entered.
     * The policy details are validated through the static method {@code validate()} of {@link  PolicyValidator}
     * Upon successful validation, the policy object is added into the {@code policyArray} list.
     *
     * @param scanner
     * @param policyArray
     */
    private static void collectTruckPolicyInput(Scanner scanner, List<Policy> policyArray) {

        boolean isTruckValid = false;
        Policy truckPolicy;
        while (!isTruckValid) {
            try {

                System.out.println("\n=== Truck Policy Input ===");
                PolicyHolder policyHolder = addNewPolicyHolder(scanner);

                System.out.print("Enter Policy Number: ");
                String policyNumber = scanner.nextLine();


                System.out.print("Enter Load Capacity: ");
                double loadCapacity = Double.parseDouble(scanner.nextLine());


                truckPolicy = new TruckPolicy(
                        policyNumber,
                        policyHolder,
                        loadCapacity
                );

                if (PolicyValidator.validate(truckPolicy)) {
                    policyArray.add(truckPolicy);
                    isTruckValid = true;
                } else {
                    System.out.println("\n[WARNING] Policy validation failed. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("\n[ERROR] Invalid input or processing error: " + e.getMessage());
                System.out.println("Please restart entering details for this policy.\n");

                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }


        }
    }

    /**
     * It is a helper function which is responsible for collecting details regarding {@link BikePolicy}
     * It keeps the input loop running till valid policy details are not entered.
     * The policy details are validated through the static method {@code validate()} of {@link  PolicyValidator}
     * Upon successful validation, the policy object is added into the {@code policyArray} list.
     *
     * @param scanner
     * @param policyArray
     */

    private static void collectBikePolicyInput(Scanner scanner, List<Policy> policyArray) {

        boolean isBikeValid = false;
        Policy bikePolicy;
        while (!isBikeValid) {
            try {
                System.out.println("\n=== Bike Policy Input ===");
                PolicyHolder policyHolder = addNewPolicyHolder(scanner);

                System.out.print("Enter Policy Number: ");
                String policyNumber = scanner.nextLine();

                System.out.print("Enter Engine Capacity (CC): ");
                int engineCapacity = Integer.parseInt(scanner.nextLine());


                bikePolicy = new BikePolicy(
                        policyNumber,
                        policyHolder,
                        engineCapacity
                );


                if (PolicyValidator.validate(bikePolicy)) {
                    policyArray.add(bikePolicy);
                    isBikeValid = true;
                } else {
                    System.out.println("\n[WARNING] Policy validation failed. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("\n[ERROR] Invalid input or processing error: " + e.getMessage());
                System.out.println("Please restart entering details for this policy.\n");

                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }


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
                System.out.println("5. View Policy Details");
                System.out.println("6. Exit");
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
                        System.out.println(policy.getPolicyDetails());
                        break;

                    case 6:
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
