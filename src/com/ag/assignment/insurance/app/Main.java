package com.ag.assignment.insurance.app;

import com.ag.assignment.insurance.model.*;
import com.ag.assignment.insurance.repository.AgeGroupFactorRepository;
import com.ag.assignment.insurance.repository.PolicyHolderRepository;
import com.ag.assignment.insurance.repository.PolicyRepository;
import com.ag.assignment.insurance.service.MotorVehiclePremiumService;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner detailsScanner = new Scanner(System.in)) {
            PolicyHolderRepository policyHolderRepository = new PolicyHolderRepository();
            AgeGroupFactorRepository ageGroupFactorRepository = new AgeGroupFactorRepository();
            PolicyRepository policyRepository = new PolicyRepository();

            MotorVehiclePremiumService premiumCalculationService = new MotorVehiclePremiumService(ageGroupFactorRepository);

            calculatePremiumFlow(policyRepository, policyHolderRepository, premiumCalculationService, detailsScanner);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void calculatePremiumFlow(PolicyRepository policyRepo, PolicyHolderRepository policyHolderRepo, MotorVehiclePremiumService premiumCalculationService, Scanner scanner){
        // Registering Policy Holder
        // Here, accepting basic user information such as name, age, state etc.

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

    public static MotorVehicle addNewVehicle(Scanner scn) {
        System.out.println("\n--- Enter Vehicle Details ---");
        scn.nextLine();

        System.out.print("Enter Vehicle Registration Number >> ");
        String vehicleNumber = scn.nextLine().trim();
        System.out.println(vehicleNumber);

        System.out.println("Select Vehicle Type (Enter respective number):");
        System.out.println("1. SUV\t2. XUV\t3. CUV\t4. SEDAN\t5. VAN\t6. CAR\t7. TRUCK\t8. BIKE");
        System.out.print(">> ");

        VehicleType vehicleType = VehicleType.getVehicleFromChoice(Integer.parseInt(scn.nextLine()));

        String fuelType = scn.nextLine().toUpperCase().trim();
        MotorVehicle vehicle = new MotorVehicle(vehicleNumber, vehicleType, fuelType);

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


        System.out.print("Enter your age >> ");
        int age = scn.nextInt();
        policyHolder.setAge(age);
        scn.nextLine();


        System.out.println("\nSelect your jurisdiction from the following choices >> ");
        System.out.println("1.Illinois(IS)\t2.Indiana(IN)\t3.Minnesota(MN) (Enter respective number)");
        System.out.print(">>");
        State personState = State.getStateFromChoice(Integer.parseInt(scn.nextLine()));
        policyHolder.setState(personState);

        System.out.println("Do you have any history of vehicle crashes? If yes, enter number of crashes >> ");
        int numberOfCrashes = scn.nextInt();
        policyHolder.setNumberOfCrashes(numberOfCrashes);

        policyHolderRepository.save(policyHolder);

        System.out.println("Account created successfully! Your userID is: " + policyHolder.getUserID());

        return policyHolder;
    }
}
