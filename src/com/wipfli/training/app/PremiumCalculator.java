package com.wipfli.training.app;

import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.PolicyBusinessException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.*;
import com.wipfli.training.service.PolicyRegister;
import com.wipfli.training.store.AgeGroupFactorStore;
import com.wipfli.training.service.NoClaimBonusCalculator;
import com.wipfli.training.service.PolicyValidator;
import com.wipfli.training.service.StandardPremiumCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class PremiumCalculator {
    public static AgeGroupFactorStore ageGroupFactorStore = new AgeGroupFactorStore();
    public static PolicyRegister policyRegisterService = new PolicyRegister();

    public static void main(String[] args) {
        try (Scanner detailsScanner = new Scanner(System.in)) {
            simulatePolicyOperations(detailsScanner);
        } catch (Exception e) {
            System.out.println(String.format("Fatal Application error received [%s] : %s", e.getClass(), e.getMessage()));
        } finally {
            System.out.println("Program finished normally...");
        }

    }

    /**
     * It is a helper function which is responsible for collecting details regarding {@link CarPolicy}
     * It keeps the input loop running till valid policy details are not entered.
     * The policy details are validated through the static method {@code validate()} of {@link  PolicyValidator}
     * Upon successful validation, the policy object is added into the {@code policyArray} list.
     *
     * @param scanner
     * @param policyHolder
     */

    private static void collectCarPolicyInput(Scanner scanner, PolicyHolder policyHolder) throws PolicyBusinessException, PolicyNotFoundException {
        String policyNumber = null;
        try {
            Policy carPolicy;

            System.out.println("\n=== Car Policy Input ===");


            System.out.print("Enter Policy Number: ");
            policyNumber = scanner.nextLine();

            System.out.print("Enter Vehicle Registration Number: ");
            String registrationNumber = scanner.nextLine();

            System.out.print("Enter expiry date of the policy (in dd/MM/yyyy format): ");
            String expiryDate = scanner.nextLine();
            LocalDate parsedExpiryDate = parseDate(expiryDate);


            carPolicy = new CarPolicy(
                    policyNumber,
                    policyHolder,
                    registrationNumber,
                    parsedExpiryDate
            );


            PolicyValidator.validate(carPolicy);
            policyRegisterService.add(carPolicy);

            System.out.println("Car Policy created successfully! Policy Details: ");
            System.out.println(
                    carPolicy.getPolicyDetails()
            );

        } catch (NumberFormatException e) {
            String finalPolicyNumber = (policyNumber != null) ? policyNumber : "UNKNOWN";
            throw new InvalidPolicyDataException(finalPolicyNumber, "Invalid Numeric Input provided!");
        } catch (DateTimeParseException e) {
            throw new InvalidPolicyDataException(
                    policyNumber,
                    String.format("Failed to parse expiry date. Dates must strictly follow the format DD/MM/YYYY using forward slashes.")
            );

        } catch (PolicyNotFoundException e) {
            throw e;
        } catch (PolicyBusinessException e) {
            throw e;
        }


    }


    /**
     * It is a helper function which is responsible for collecting details regarding {@link TruckPolicy}
     * It keeps the input loop running till valid policy details are not entered.
     * The policy details are validated through the static method {@code validate()} of {@link  PolicyValidator}
     * Upon successful validation, the policy object is added into the {@code policyArray} list.
     *
     * @param scanner
     * @param policyHolder
     */
    private static void collectTruckPolicyInput(Scanner scanner, PolicyHolder policyHolder) throws PolicyBusinessException, PolicyNotFoundException {

        String policyNumber = null;
        try {
            Policy truckPolicy;

            System.out.println("\n=== Truck Policy Input ===");

            System.out.print("Enter Policy Number: ");
            policyNumber = scanner.nextLine();


            System.out.print("Enter Load Capacity: ");
            double loadCapacity = Double.parseDouble(scanner.nextLine());

            System.out.println("Enter expiry date of the policy (in dd/MM/yyyy format): ");
            String expiryDate = scanner.nextLine();

            LocalDate parsedExpiryDate = parseDate(expiryDate);


            truckPolicy = new TruckPolicy(
                    policyNumber,
                    policyHolder,
                    loadCapacity,
                    parsedExpiryDate
            );
            PolicyValidator.validate(truckPolicy);
            policyRegisterService.add(truckPolicy);

            System.out.println("Truck Policy created successfully! Policy Details: ");
            System.out.println(
                    truckPolicy.getPolicyDetails()
            );

        } catch (DateTimeParseException e) {
            throw new InvalidPolicyDataException(
                    policyNumber,
                    String.format("Failed to parse expiry date. Dates must strictly follow the format DD/MM/YYYY using forward slashes and ensure that input date is valid one.")
            );
        } catch (NumberFormatException e) {
            String finalPolicyNumber = (policyNumber != null) ? policyNumber : "UNKNOWN";
            throw new InvalidPolicyDataException(finalPolicyNumber, "Invalid Numeric Input provided!");
        } catch (PolicyNotFoundException e) {
            throw e;
        } catch (PolicyBusinessException e) {
            throw e;
        }


    }

    /**
     * It is a helper function which is responsible for collecting details regarding {@link BikePolicy}
     * It keeps the input loop running till valid policy details are not entered.
     * The policy details are validated through the static method {@code validate()} of {@link  PolicyValidator}
     * Upon successful validation, the policy object is added into the {@code policyArray} list.
     *
     * @param scanner
     */

    private static void collectBikePolicyInput(Scanner scanner, PolicyHolder policyHolder) throws PolicyBusinessException, PolicyNotFoundException {
        String policyNumber = null;
        try {
            Policy bikePolicy;
            System.out.println("\n=== Bike Policy Input ===");

            System.out.print("Enter Policy Number: ");
            policyNumber = scanner.nextLine();

            System.out.println("Enter expiry date of the policy (in dd/MM/yyyy format): ");
            String expiryDate = scanner.nextLine();
            LocalDate parsedExpiryDate = parseDate(expiryDate);


            System.out.print("Enter Engine Capacity (CC): ");
            int engineCapacity = Integer.parseInt(scanner.nextLine());


            bikePolicy = new BikePolicy(
                    policyNumber,
                    policyHolder,
                    engineCapacity, parsedExpiryDate
            );

            PolicyValidator.validate(bikePolicy);
            policyRegisterService.add(bikePolicy);

            System.out.println("Bike Policy created successfully! Policy Details: ");

            System.out.println(
                    bikePolicy.getPolicyDetails()
            );

        } catch (DateTimeParseException e) {
            throw new InvalidPolicyDataException(
                    policyNumber,
                    String.format("Failed to parse expiry date. Dates must strictly follow the format DD/MM/YYYY using forward slashes.")
            );
        } catch (NumberFormatException e) {
            String finalPolicyNumber = (policyNumber != null) ? policyNumber : "UNKNOWN";
            throw new InvalidPolicyDataException(finalPolicyNumber, "Invalid Numeric Input provided!");
        } catch (PolicyNotFoundException e) {
            throw e;
        } catch (PolicyBusinessException e) {
            throw e;
        }
    }

    /**
     * This is a helper function which helps in completion of following tasks:
     * 1. Creation of PolicyHolder Object
     * 2. Accepting Vehicle Type as input and calling the respective Policy input helper functions: {@code collectTruckPolicyInput} {@code collectCarPolicyInput} {@code collectBikePolicyInput}
     *
     * @param detailsScanner
     * @throws PolicyNotFoundException
     * @throws PolicyBusinessException
     */
    public static void simulatePolicyDetailsInput(Scanner detailsScanner) throws PolicyNotFoundException, PolicyBusinessException {
        PolicyHolder policyHolder = addNewPolicyHolder(detailsScanner);

        System.out.println("Select Vehicle Type (Enter respective number):");
        System.out.println("1.CAR\t2.TRUCK\t3.BIKE");
        System.out.print(">> ");

        VehicleType vehicleType = VehicleType.getVehicleFromChoice(Integer.parseInt(detailsScanner.nextLine().trim()));
        switch (vehicleType) {
            case CAR -> collectCarPolicyInput(detailsScanner, policyHolder);
            case BIKE -> collectBikePolicyInput(detailsScanner, policyHolder);
            case TRUCK -> collectTruckPolicyInput(detailsScanner, policyHolder);
        }

    }


    /**
     * This is a helper function which helps in simulating different operations on the policy through a menu-driven program.
     *
     * @param detailsScanner
     * @throws PolicyNotFoundException
     * @throws PolicyBusinessException
     */
    public static void simulatePolicyOperations(Scanner detailsScanner) {
        boolean exit = false;
        Policy currentPolicy = null;
        String policyNumber = null;
        System.out.println("--------------Welcome to Policy Management system!--------------");

        while (!exit) {
            try {

                PremiumCalculable standardPremiumCalculator = new StandardPremiumCalculator(ageGroupFactorStore);
                PremiumCalculable noClaimBonusCalculator = new NoClaimBonusCalculator(ageGroupFactorStore);


                System.out.println("\n================ POLICY OPERATIONS MENU ================");
                System.out.println("1. Create And Register New Policy");
                System.out.println("2. Select Policy");
                System.out.println("3. Compute / View Premium");
                System.out.println("4. Record a Claim (Increment Claims)");
                System.out.println("5. Expire Policy");
                System.out.println("6. Renew Policy");
                System.out.println("7. View Policy Details");
                System.out.println("8. Exit");
                System.out.print("Enter choice >> ");

                int choice = Integer.parseInt(detailsScanner.nextLine());


                switch (choice) {
                    case 1:
                        simulatePolicyDetailsInput(detailsScanner);
                        break;

                    case 2:
                        System.out.println("Enter policy number >> ");
                        policyNumber = detailsScanner.nextLine();
                        currentPolicy = policyRegisterService.findByNumber(policyNumber);
                        System.out.println("Policy selected successfully: " + currentPolicy.getPolicyNumber());
                        break;

                    case 3:
                        verifyPolicySelected(currentPolicy);
                        System.out.println("\n[ACTION] Computing Premium...");
                        BigDecimal standardPremium = standardPremiumCalculator.calculatePremium(currentPolicy);
                        BigDecimal discountedPremium = noClaimBonusCalculator.calculatePremium(currentPolicy);
                        System.out.println("Standard Premium >> " + standardPremium);
                        System.out.println("Discounted Premium >> " + discountedPremium);
                        break;

                    case 4:
                        verifyPolicySelected(currentPolicy);
                        System.out.println("\n[ACTION] Recording Claim...");
                        currentPolicy.recordClaim();
                        System.out.println("\n[ACTION] recordClaim() called - claims is now " + currentPolicy.getClaimsCount());
                        break;

                    case 5:
                        verifyPolicySelected(currentPolicy);
                        System.out.println("\n[ACTION] Expiring Policy...");
                        currentPolicy.expire();
                        break;

                    case 6:
                        verifyPolicySelected(currentPolicy);
                        System.out.println("\n[ACTION] Renewing Policy...");
                        System.out.println("Enter date of renewal (in dd/MM/yyyy format): ");
                        String renewalDate = detailsScanner.nextLine();
                        LocalDate parsedRenewalDate = parseDate(renewalDate);
                        currentPolicy.renew(parsedRenewalDate);
                        break;

                    case 7:
                        verifyPolicySelected(currentPolicy);
                        System.out.println(currentPolicy.getPolicyDetails());
                        break;

                    case 8:
                        System.out.println("\nExiting System. Goodbye!");
                        exit = true;
                        break;

                    default:
                        System.out.println("\n[ERROR] Invalid option. Please select 1-5.");
                        break;
                }

            } catch (NumberFormatException e) {
                String finalPolicyNumber = (policyNumber != null) ? policyNumber : "UNKNOWN";
                throw new InvalidPolicyDataException(finalPolicyNumber, "Invalid Numeric Input provided!");
            } catch (PolicyNotFoundException e) {
                System.out.println(
                        String.format("REFUSED [%s] %s : %s", e.getClass().getSimpleName(), e.getPolicyNumber(), e.getMessage())
                );
            } catch (PolicyBusinessException e) {
                System.out.println(
                        String.format("REFUSED [%s] %s: %s", e.getClass().getSimpleName(), e.getPolicyNumber(), e.getMessage())
                );
            }


        }
    }


    private static void verifyPolicySelected(Policy currentPolicy) throws InvalidPolicyDataException {
        if (currentPolicy == null) {
            throw new InvalidPolicyDataException(
                    "UNKNOWN",
                    "Please select a policy first.");
        }
    }

    /**
     * It parses date in the required format and checks for any invalid date strings. Returns {@link LocalDate} object parsed from the given input string.
     *
     * @param date
     * @return {@link LocalDate parsedDate}
     */

    private static LocalDate parseDate(String date) {
        String dateFormat = "dd/MM/uuuu";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withResolverStyle(ResolverStyle.STRICT);
        LocalDate parsedDate = LocalDate.parse(date, formatter);

        return parsedDate;
    }

    public static PolicyHolder addNewPolicyHolder(Scanner scn) {
        try {
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
            System.out.print("1.Illinois(IS)\t2.Indiana(IN)\t3.Minnesota(MN) (Enter respective number): ");
            System.out.print(">>");
            State personState = State.getStateFromChoice(Integer.parseInt(scn.nextLine().trim()));


            PolicyHolder policyHolder = new PolicyHolder(firstName, lastName, age, personState);
            System.out.println("Account created successfully! Your userID is: " + policyHolder.getUserID());

            return policyHolder;
        } catch (IllegalArgumentException e) {
            throw new InvalidPolicyDataException("UNKNOWN",
                    String.format("Invalid input provided for policyholder details!\nError: %s", e.getMessage(), e)
            );
        }

    }
}
