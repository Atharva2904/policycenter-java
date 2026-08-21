package com.wipfli.training.app;

import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.PolicyBusinessException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.*;
import com.wipfli.training.service.PolicyRegister;
import com.wipfli.training.service.PolicyValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PremiumCalculator {
    public static PolicyRegister policyRegisterService = new PolicyRegister();

    public static void main(String[] args) {
            try (Scanner detailsScanner = new Scanner(System.in)) {
                run(detailsScanner);
            } catch (Exception e) {
                System.out.printf("%nFATAL APPLICATION ERROR [%s]: %s%n", e.getClass().getSimpleName(), e.getMessage());
            } finally {
                printSeparator();
                System.out.println("Program finished normally.");
            }
        }


    /**
     * Simulates a Policy Register menu with options to add/view policies
     * and view premium summaries grouped by vehicle type.
     *
     * @param detailsScanner {@link Scanner} object that reads input from the console
     */
    private static void run(Scanner detailsScanner)  {
        boolean exit = false;
        boolean isSeeded = false;
        printHeader("Policy Register");

        while (!exit) {
            try {
                if(!isSeeded) {
                    seedDemoPolicies();
                    isSeeded = true;
                }

                System.out.println();
                System.out.println("----- Main Menu -----");
                System.out.println("  1. Add Policy");
                System.out.println("  2. View Policy Details");
                System.out.println("  3. View Policies for a Customer");
                System.out.println("  4. View Policies by Vehicle Type");
                System.out.println("  5. View Policies Expiring Soon");
                System.out.println("  6. View Premium Summary by Vehicle Type");
                System.out.println("  7. View Number of Policies by Vehicle Type");
                System.out.println("  8. View Customer with Most Policies");
                System.out.println("  9. View Top 5 Policies by Premium");
                System.out.println("  10. View Customers having similar policy records");
                System.out.println("  11. Clear Console");
                System.out.println("  12. Exit");
                System.out.print("Enter choice >> ");

                int choice = Integer.parseInt(detailsScanner.nextLine().trim());

                switch (choice) {
                    case 1 -> simulatePolicyDetailsInput(detailsScanner);
                    case 2 -> handleViewPolicyDetails(detailsScanner);
                    case 3 -> handleViewPoliciesForCustomer(detailsScanner);
                    case 4 -> handleViewPoliciesByVehicleType(detailsScanner);
                    case 5 -> handleViewPoliciesExpiringSoon(detailsScanner);
                    case 6 -> handleViewSummaryByVehicleType();
                    case 7 -> handleNumberOfPoliciesPerVehicleType();
                    case 8 -> handleViewCustomerWithMostPolicies();
                    case 9 -> handleViewHighestPremiumPolicies();
                    case 10 -> handleViewCustomersWithSimilarPolicyRecords(detailsScanner);
                    case 11 -> clearConsole();
                    case 12 -> {
                        System.out.println();
                        printSuccess("Exiting Policy Register. Goodbye!");
                        exit = true;
                    }
                    default -> printError("Invalid option. Please select 1-12.");
                }

            } catch (NumberFormatException e) {
                throw new InvalidPolicyDataException("UNKNOWN", "Invalid numeric input provided.");
            }
            catch (PolicyNotFoundException e) {
                System.out.printf("REFUSED [%s] Policy: %s | %s%n",
                        e.getClass().getSimpleName(), e.getPolicyNumber(), e.getMessage());
            } catch (PolicyBusinessException e) {
                printRefused(e);
            }
        }
    }

    private static void handleViewPolicyDetails(Scanner scanner){
        System.out.println("Enter Policy Number >> ");
        String policyNumber = scanner.nextLine().trim();
        Optional<Policy> currentPolicy = policyRegisterService.findByNumber(policyNumber);

        if(currentPolicy.isPresent()){
            System.out.println(currentPolicy.get().getPolicyDetails());
        }
        else{
            System.out.println("No policy found with number: " + policyNumber);
        }
    }

    private static void handleViewPoliciesForCustomer(Scanner scanner) throws PolicyNotFoundException {
        System.out.print("Enter customer first name >> ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter customer last name >> ");
        String lastName = scanner.nextLine().trim();

        String customerName = firstName + " " + lastName;
        List<Policy> customerPolicies = policyRegisterService.findByCustomer(customerName);

        printHeader("Policies for " + customerName);
        if (customerPolicies.isEmpty()) return;

        for(Policy p: customerPolicies){
            System.out.println(p.getPolicyDetails());
            printSeparator();
        }
    }

    private static void handleViewPoliciesByVehicleType(Scanner scanner) throws PolicyNotFoundException {
        System.out.println("Select Vehicle Type:");
        System.out.println("  1. CAR");
        System.out.println("  2. TRUCK");
        System.out.println("  3. BIKE");
        System.out.print("Enter choice >> ");

        VehicleType vehicleType = VehicleType.getVehicleFromChoice(Integer.parseInt(scanner.nextLine().trim()));
        List<Policy> vehiclePolicies = policyRegisterService.findByVehicleType(vehicleType);
        printHeader("Found " + vehiclePolicies.size() + " policies for " + vehicleType);

        if (vehiclePolicies.isEmpty()) return;

        for(Policy p: vehiclePolicies){
            System.out.println(p.getPolicyDetails());
            printSeparator();
        }
    }

    private static void handleViewPoliciesExpiringSoon(Scanner scanner) {
        System.out.print("Enter number of days >> ");
        int days = Integer.parseInt(scanner.nextLine().trim());


        List<Policy> expiringPolicies = policyRegisterService.findExpiringWithinDays(days);

        printHeader("Policies Expiring Within " + days + " Days");
        if (expiringPolicies.isEmpty()) {
            System.out.println("No policies expiring within the specified period.");
            return;
        }

        for(Policy p: expiringPolicies){
            System.out.printf("Policy: %-15s | Expiry: %s%n", p.getPolicyNumber(), p.getExpiryDate());
        }

        printSeparator();

    }

    private static void handleViewSummaryByVehicleType() {
        Hashtable<VehicleType, Double> premiumSummary = policyRegisterService.totalPremiumByVehicleType();

        printHeader("Premium Summary by Vehicle Type");
        if (premiumSummary.isEmpty()) {
            System.out.println("No policies available for premium summary.");
            return;
        }

        for(Map.Entry<VehicleType, Double> entry : premiumSummary.entrySet()) {
            System.out.printf("%-10s >> $%,.2f%n", entry.getKey(), entry.getValue());
        }
        printSeparator();
    }

    private static void handleNumberOfPoliciesPerVehicleType(){
        Map<VehicleType, Integer> policyCountPerVehicleType = policyRegisterService.countPoliciesByVehicleType();

        if(policyCountPerVehicleType.isEmpty()){
            System.out.println("No policies available.");
            return;
        }

        printHeader("Number of Policies by Vehicle Type");
        policyCountPerVehicleType.forEach(((vehicleType, integer) -> {
            System.out.printf("%-10s --> %d%n", vehicleType, integer);
        }));

        printSeparator();
    }

    private static void handleViewCustomerWithMostPolicies(){
        Optional<String> customerWithMostPolicies = policyRegisterService.getCustomerWithMostPolicies();

        if(!customerWithMostPolicies.isPresent()){
            System.out.println("No customer found with most policies.");
            return;
        }

        printHeader("Customer with most no. of policies: ");
        System.out.println(customerWithMostPolicies.get());
        printSeparator();

    }

    private static void handleViewHighestPremiumPolicies(){
        Map<String, Double> highestPremiumPolicies = policyRegisterService.getTop5PoliciesByPremium();

        if(highestPremiumPolicies.isEmpty()){
            System.out.println("No policies found.");
            return;
        }

        printHeader("Top 5 Policies by Premium");

        // Java's Lambda expressions can only access local variables which are "effectively final"
        // A final variable is the one whose value never changes after initialization

        // Instead we can create an object of AtomicInteger which holds an integer value inside it.
        // Since the data that it holds is modified and not the reference to the object itself, it is allowed to be used inside a lambda expression.

        AtomicInteger count = new AtomicInteger(1);


        highestPremiumPolicies.forEach((policyNumber, premium) -> {
                    System.out.printf("%d] %s%n", count.getAndIncrement(), policyNumber);
                    System.out.printf("Premium: $%,.2f%n", premium);
                }
                );


        printSeparator();


    }

    private static void handleViewCustomersWithSimilarPolicyRecords(Scanner scanner){
        System.out.print("Enter customer first name >> ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter customer last name >> ");
        String lastName = scanner.nextLine().trim();

        String customerName = firstName + " " + lastName;

        List<String> customersWithSimilarPolicyRecords = policyRegisterService.getCustomersWithMatchingPattern(customerName);
        if (customersWithSimilarPolicyRecords.isEmpty()) {
            System.out.println("No customers found with similar policy records.");
        } else {
            printHeader("Customers with similar policy records");
            customersWithSimilarPolicyRecords.forEach(System.out::println);
            printSeparator();
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
            printHeader("Car Policy Input");

            System.out.print("Enter policy number >> ");
            policyNumber = scanner.nextLine().trim();

            System.out.print("Enter vehicle registration number >> ");
            String registrationNumber = scanner.nextLine().trim();

            System.out.print("Enter expiry date (dd/MM/yyyy) >> ");
            LocalDate parsedExpiryDate = parseDate(scanner.nextLine().trim());

            CarPolicy carPolicy = new CarPolicy(policyNumber, policyHolder, registrationNumber, parsedExpiryDate);

            PolicyValidator.validate(carPolicy);
            policyRegisterService.add(carPolicy);

            printSuccess("Car policy created successfully.");
            System.out.println(carPolicy.getPolicyDetails());

        } catch (NumberFormatException e) {
            throw new InvalidPolicyDataException(
                    policyNumber != null ? policyNumber : "UNKNOWN",
                    "Invalid numeric input provided."
            );
        } catch (DateTimeParseException e) {
            throw new InvalidPolicyDataException(
                    policyNumber,
                    "Failed to parse expiry date. Dates must follow DD/MM/YYYY using forward slashes."
            );
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
            printHeader("Truck Policy Input");

            System.out.print("Enter policy number >> ");
            policyNumber = scanner.nextLine().trim();

            System.out.print("Enter load capacity (tons) >> ");
            double loadCapacity = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter expiry date (dd/MM/yyyy) >> ");
            LocalDate parsedExpiryDate = parseDate(scanner.nextLine().trim());

            TruckPolicy truckPolicy = new TruckPolicy(policyNumber, policyHolder, loadCapacity, parsedExpiryDate);

            PolicyValidator.validate(truckPolicy);
            policyRegisterService.add(truckPolicy);

            printSuccess("Truck policy created successfully.");
            System.out.println(truckPolicy.getPolicyDetails());

        } catch (DateTimeParseException e) {
            throw new InvalidPolicyDataException(
                    policyNumber,
                    "Failed to parse expiry date. Dates must follow DD/MM/YYYY using forward slashes and be valid."
            );
        } catch (NumberFormatException e) {
            throw new InvalidPolicyDataException(
                    policyNumber != null ? policyNumber : "UNKNOWN",
                    "Invalid numeric input provided."
            );
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
            printHeader("Bike Policy Input");

            System.out.print("Enter policy number >> ");
            policyNumber = scanner.nextLine().trim();

            System.out.print("Enter expiry date (dd/MM/yyyy) >> ");
            LocalDate parsedExpiryDate = parseDate(scanner.nextLine().trim());

            System.out.print("Enter engine capacity (CC) >> ");
            int engineCapacity = Integer.parseInt(scanner.nextLine().trim());

            BikePolicy bikePolicy = new BikePolicy(policyNumber, policyHolder, engineCapacity, parsedExpiryDate);

            PolicyValidator.validate(bikePolicy);
            policyRegisterService.add(bikePolicy);

            printSuccess("Bike policy created successfully.");
            System.out.println(bikePolicy.getPolicyDetails());

        } catch (DateTimeParseException e) {
            throw new InvalidPolicyDataException(
                    policyNumber,
                    "Failed to parse expiry date. Dates must follow DD/MM/YYYY using forward slashes."
            );
        } catch (NumberFormatException e) {
            throw new InvalidPolicyDataException(
                    policyNumber != null ? policyNumber : "UNKNOWN",
                    "Invalid numeric input provided."
            );
        }
    }

    /**
     * This is a helper function which helps in completion of following tasks:
     * 1. Creation of PolicyHolder Object
     * 2. Accepting Vehicle Type as input and calling the respective Policy input helper functions: {@code collectTruckPolicyInput} {@code collectCarPolicyInput} {@code collectBikePolicyInput}
     *
     * @param detailsScanner {@link Scanner} object that reads input from the console
     * @throws PolicyNotFoundException
     * @throws PolicyBusinessException
     */
    public static void simulatePolicyDetailsInput(Scanner detailsScanner) throws PolicyNotFoundException, PolicyBusinessException   {
        PolicyHolder policyHolder = addNewPolicyHolder(detailsScanner);

        System.out.println();
        System.out.println("Select Vehicle Type:");
        System.out.println("  1. CAR");
        System.out.println("  2. TRUCK");
        System.out.println("  3. BIKE");
        System.out.print("Enter choice >> ");

        VehicleType vehicleType = VehicleType.getVehicleFromChoice(Integer.parseInt(detailsScanner.nextLine().trim()));

        switch (vehicleType) {
            case CAR -> collectCarPolicyInput(detailsScanner, policyHolder);
            case BIKE -> collectBikePolicyInput(detailsScanner, policyHolder);
            case TRUCK -> collectTruckPolicyInput(detailsScanner, policyHolder);
        }
    }

    public static PolicyHolder addNewPolicyHolder(Scanner scn) {
        try {
            printHeader("New Policy Holder");

            System.out.print("Enter first name >> ");
            String firstName = scn.nextLine().trim();

            System.out.print("Enter last name >> ");
            String lastName = scn.nextLine().trim();

            System.out.print("Enter age >> ");
            int age = scn.nextInt();
            scn.nextLine();
            PolicyValidator.validateAge(age);

            System.out.println();
            System.out.println("Select jurisdiction:");
            System.out.println("  1. Illinois (IS)");
            System.out.println("  2. Indiana (IN)");
            System.out.println("  3. Minnesota (MN)");
            System.out.print("Enter choice >> ");
            State personState = State.getStateFromChoice(Integer.parseInt(scn.nextLine().trim()));

            PolicyHolder policyHolder = new PolicyHolder(firstName, lastName, age, personState);
            printSuccess("Account created. Your user ID is: " + policyHolder.getUserID());

            return policyHolder;

        } catch (IllegalArgumentException e) {
            throw new InvalidPolicyDataException(
                    "UNKNOWN",
                    String.format("Invalid policyholder details: %s", e.getMessage())
            );
        }
    }

    /**
     * It parses date in the required format and checks for any invalid date strings. Returns {@link LocalDate} object parsed from the given input string.
     *
     * @param date
     * @return {@link LocalDate parsedDate}
     */

    private static LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(date, formatter);
    }

    private static void printHeader(String title){
        int width = 60;
        String padded = " " + title + " ";
        int left = (width - padded.length()) / 2;
        int right = width - padded.length() - left;
        System.out.println();
        System.out.println("=".repeat(width));
        System.out.println(" ".repeat(Math.max(0, left)) + padded + " ".repeat(Math.max(0, right)));
        System.out.println("=".repeat(width));
    }

    private static void printSeparator() {
        System.out.println("-".repeat(60));
    }

    private static void printSuccess(String message) {
        System.out.println("[OK] " + message);
    }

    private static void printError(String message) {
        System.out.println("[FAIL] ERROR: " + message);
    }

    private static void printRefused(PolicyBusinessException e) {
        System.out.printf("REFUSED [%s] Policy: %s | %s%n",
                e.getClass().getSimpleName(), e.getPolicyNumber(), e.getMessage());
    }

    private static void clearConsole() {
        System.out.println("\n".repeat(25));
    }


    private static void seedDemoPolicies() throws PolicyNotFoundException{
        PolicyHolder userRavi = new PolicyHolder("Ravi", "Kumar", 22, State.IS);
        PolicyHolder userMeena = new PolicyHolder("Meena", "Iyer", 34, State.IN);
        PolicyHolder userAjay = new PolicyHolder("Ajay", "Verma", 41, State.MN);
        PolicyHolder userSneha = new PolicyHolder("Sneha", "Rao", 29, State.IS);

        CarPolicy car1 = new CarPolicy("POL-2001", userRavi, "IL-ABC-123", 1, LocalDate.of(2026, 8, 15));
        CarPolicy car2 = new CarPolicy("POL-2004", userAjay, "IN-XYZ-789", 0, LocalDate.of(2027, 1, 10));
        CarPolicy car3 = new CarPolicy("POL-2006", userRavi, "IL-ABC-123", 3, LocalDate.of(2026, 12, 1));


        BikePolicy bike1 = new BikePolicy("POL-2003", userMeena, 150, 2, LocalDate.of(2026, 8, 20));
        BikePolicy bike2 = new BikePolicy("POL-2005", userSneha, 650, 0,  LocalDate.of(2026, 8, 25));


        TruckPolicy truck1 = new TruckPolicy("POL-2002", userRavi, 12.5,0, LocalDate.of(2026, 9, 1));


        List<Policy> demoPolicies = List.of(car1, car2, bike1, bike2, truck1, car3);

        for (Policy policy : demoPolicies) {
            PolicyValidator.validate(policy);
            policyRegisterService.add(policy);
        }

        printSuccess("Seeded " + demoPolicies.size() + " demo policies.");
    }


}
