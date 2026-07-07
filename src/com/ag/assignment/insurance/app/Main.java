package com.ag.assignment.insurance.app;

import com.ag.assignment.insurance.model.*;
import com.ag.assignment.insurance.repository.AgeGroupFactorRepository;
import com.ag.assignment.insurance.repository.PolicyHolderRepository;
import com.ag.assignment.insurance.repository.PolicyRepository;
import com.ag.assignment.insurance.repository.VehicleFactorRepository;
import com.ag.assignment.insurance.service.PremiumCalculationService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            PolicyHolderRepository policyHolderRepository = new PolicyHolderRepository();
            AgeGroupFactorRepository ageGroupFactorRepository = new AgeGroupFactorRepository();
            VehicleFactorRepository vehicleFactorRepository = new VehicleFactorRepository();
            PolicyRepository policyRepository = new PolicyRepository();

            PremiumCalculationService premiumCalculationService = new PremiumCalculationService(vehicleFactorRepository, ageGroupFactorRepository);


            Scanner detailsScanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("\n===== INSURANCE MANAGEMENT SYSTEM =====");
                System.out.println("1. Create Policy & Calculate Premium");
                System.out.println("2. Check Policy Status (Active/Expired/Due)");
                System.out.println("3. Manage Customer Names (Sorted & Unique)");
                System.out.println("4. Identify Duplicate Policy Numbers");
                System.out.println("5. Exit");
                System.out.print("Enter your choice (1-5): ");

                try {
                    int choice = Integer.parseInt(detailsScanner.nextLine());

                    switch (choice) {
                        case 1:
                            calculatePremiumFlow(policyRepository, policyHolderRepository, premiumCalculationService, detailsScanner);
                            break;
                        case 2:
                            checkPolicyStatusFlow(policyRepository, detailsScanner);
                            break;
                        case 3:
                            manageCustomerNamesFlow(policyHolderRepository, detailsScanner);
                            break;
                        case 4:
                            findDuplicatePoliciesFlow(policyRepository, detailsScanner);
                            break;
                        case 5:
                            System.out.println("Exiting...");
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid choice! Please choose between 1 to 5.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Please enter a valid number.");
                }
                catch (IllegalArgumentException e){
                    System.out.println("Invalid argument provided! Error message: " + e.getMessage());
                }
                catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
            }

            detailsScanner.close();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void calculatePremiumFlow(PolicyRepository policyRepo, PolicyHolderRepository policyHolderRepo, PremiumCalculationService premiumCalculationService, Scanner scanner){
        // Registering Policy Holder
        PolicyHolder policyHolder = addNewPolicyHolder(policyHolderRepo, scanner);

        // Registering vehicle details
        MotorVehicle motorVehicle = addNewVehicle(scanner);

        // Creating Policy
        MotorVehiclePolicy motorVehiclePolicy = new MotorVehiclePolicy(policyHolder, motorVehicle);
        policyRepo.save(motorVehiclePolicy);

        // Computing Premium
        BigDecimal premium = premiumCalculationService.calculatePremium(motorVehiclePolicy);
        System.out.println("Premium: " + premium);
    }

    private static void checkPolicyStatusFlow(PolicyRepository policyRepo, Scanner scanner){
        System.out.println("\n------ Check Policy Status ---------");
        System.out.println("Enter policy number >> ");

        BigInteger policyNumber = scanner.nextBigInteger();
        Policy policyToCheck = policyRepo.getPolicyByNumber(policyNumber);

    }

    private static void findDuplicatePoliciesFlow(PolicyRepository policyRepo, Scanner scanner){
        System.out.println("\n--- Duplicate Policy Numbers ---");
        List<Policy> duplicatedPolicies = new ArrayList<>();

        duplicatedPolicies.add(new MotorVehiclePolicy(BigInteger.valueOf(10000)));
        duplicatedPolicies.add(new MotorVehiclePolicy(BigInteger.valueOf(20000)));
        duplicatedPolicies.add(new MotorVehiclePolicy(BigInteger.valueOf(20000)));
        duplicatedPolicies.add(new MotorVehiclePolicy(BigInteger.valueOf(30000)));
        duplicatedPolicies.add(new MotorVehiclePolicy(BigInteger.valueOf(70000)));
        duplicatedPolicies.add(new MotorVehiclePolicy(BigInteger.valueOf(10000)));
        duplicatedPolicies.add(new MotorVehiclePolicy(BigInteger.valueOf(10000)));



        Set<Policy> duplicates = policyRepo.findDuplicatePolicies(duplicatedPolicies);

        System.out.println("Duplicated policies");

        for(Policy p : duplicates){
            System.out.println("Policy Number: " + p.getPolicyNumber());
        }

    }

    private static void manageCustomerNamesFlow(PolicyHolderRepository policyHolderRepo, Scanner scanner){
        List<PolicyHolder> allPolicyHolders = policyHolderRepo.getAllPolicyHolders();

        List<PolicyHolder> uniqueSortedList = allPolicyHolders.stream().distinct().sorted().toList();

        System.out.println("Policy Holder Names >> ");
        uniqueSortedList.forEach((e) -> System.out.println(e.getDisplayName()));
    }

    public static MotorVehicle addNewVehicle(Scanner scn) {
        System.out.println("\n--- Enter Vehicle Details ---");

        System.out.print("Enter Vehicle Registration Number >> ");
        String vehicleNumber = scn.nextLine().trim();

        System.out.println("Select Vehicle Type:");
        System.out.println("1. SEDAN\t2. SUV\t3. CUV\t4. XUV\t5. VAN");
        System.out.print(">> ");
        String typeInput = scn.nextLine().toUpperCase().trim();
        VehicleType vehicleType = VehicleType.valueOf(typeInput);

        System.out.print("Enter Fuel Type (PETROL, DIESEL, ELECTRIC) >> ");
        String fuelType = scn.nextLine().toUpperCase().trim();

        System.out.print("Enter Vehicle Market Value ($) >> ");
        String marketValueInput = scn.nextLine().trim();
        BigDecimal marketValue = new BigDecimal(marketValueInput);

        if (marketValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Market value must be greater than zero!");
        }

        MotorVehicle vehicle = new MotorVehicle(vehicleNumber, vehicleType, fuelType, marketValue);

        System.out.println("Vehicle initialized successfully for number: " + vehicle.getVehicleNumber());

        return vehicle;
    }

    public static PolicyHolder addNewPolicyHolder(PolicyHolderRepository policyHolderRepository, Scanner scn) {
        PolicyHolder policyHolder = new PolicyHolder();
        String firstName, middleName, lastName;

        System.out.println("Enter your first name >> ");
        firstName = scn.nextLine();
        policyHolder.setFirstName(firstName);

        System.out.println("Enter your middle name >> ");
        middleName = scn.nextLine();
        policyHolder.setMiddleName(middleName);

        System.out.println("Enter your last name >> ");
        lastName = scn.nextLine();
        policyHolder.setLastName(lastName);

        System.out.print("Enter your Date of Birth in dd/MM/yyyy format >> ");
        String dob = scn.nextLine();
        policyHolder.setDateOfBirth(dob);


        System.out.println("\nSelect your jurisdiction from the following choices >> ");
        System.out.println("1.Illinois(IS)\t2.Indiana(IN)\t3.Minnesota(MN)");
        System.out.print(">>");
        State personState = State.valueOf(scn.nextLine());
        policyHolder.setState(personState);

        policyHolderRepository.save(policyHolder);

        System.out.println("Account created successfully! Your userID is: " + policyHolder.getUserID());

        return policyHolder;
    }
}
