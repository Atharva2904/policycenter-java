package com.wipfli.training.model;

import com.wipfli.training.dto.PolicyRequest;
import com.wipfli.training.dto.PolicyResponse;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.service.NoClaimBonusCalculator;
import com.wipfli.training.service.PolicyFactory;
import com.wipfli.training.service.PolicyRegister;
import com.wipfli.training.service.PolicyValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyRegister policyRegisterService;

    public PolicyController(PolicyRegister policyRegisterService) {
        this.policyRegisterService = policyRegisterService;
    }

    @PostMapping
    public ResponseEntity<PolicyResponse> addPolicy(@RequestBody PolicyRequest request) throws PolicyNotFoundException {
        PolicyHolder holder = new PolicyHolder(
                request.holderFirstName(),
                request.holderLastName(),
                request.holderAge(),
                request.holderState()
        );

        PolicyValidator.validateAge(request.holderAge());

        LocalDate expiryDate = parseDate(request.expiryDate());

        Policy policy = switch (request.vehicleType()) {
            case CAR -> PolicyFactory.createCarPolicy(
                    request.policyNumber(),
                    holder,
                    request.registrationNumber(),
                    request.claimsCount(),
                    expiryDate
            );
            case BIKE -> PolicyFactory.createBikePolicy(
                    request.policyNumber(),
                    holder,
                    request.engineCC(),
                    request.claimsCount(),
                    expiryDate
            );
            case TRUCK -> PolicyFactory.createTruckPolicy(
                    request.policyNumber(),
                    holder,
                    request.loadCapacity(),
                    request.claimsCount(),
                    expiryDate
            );
        };

        PolicyValidator.validate(policy);

        System.out.println("Adding policy: " + policy + " for customer: " + holder.getDisplayName());
        policyRegisterService.add(policy);

        return ResponseEntity.ok(toResponse(policy));
    }

    private PolicyResponse toResponse(Policy policy) {
        return new PolicyResponse(
                policy.getPolicyNumber(),
                policy.getPolicyStatus(),
                policy.getVehicleType(),
                policy.getClaimsCount(),
                policy.getPolicyHolder().getDisplayName(),
                policy.getExpiryDate()
        );
    }

    @GetMapping("/{policyNumber}")
    public ResponseEntity<PolicyResponse> viewPolicyDetails(@PathVariable String policyNumber) throws PolicyNotFoundException {
        Policy policy = policyRegisterService.findByNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException(policyNumber, "No policy found with number: " + policyNumber));

//        return ResponseEntity.ok(policy);

        return ResponseEntity.ok(toResponse(policy));
    }



    @GetMapping("/{policyNumber}/premium")
    public ResponseEntity<Double> getPremium(
            @PathVariable String policyNumber) throws PolicyNotFoundException {

        double premium =
                policyRegisterService.getPremium(policyNumber);
        return ResponseEntity.ok(premium);
    }


    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<PolicyResponse>> viewPoliciesForCustomer(@PathVariable String customerName) {
        List<PolicyResponse> responses = policyRegisterService.findByCustomer(customerName).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/vehicle-type/{vehicleType}")
    public ResponseEntity<List<PolicyResponse>> viewPoliciesByVehicleType(@PathVariable VehicleType vehicleType) {
        List<PolicyResponse> responses = policyRegisterService.findByVehicleType(vehicleType).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<PolicyResponse>> viewPoliciesExpiringSoon(@RequestParam int days) {
        List<PolicyResponse> responses = policyRegisterService.findExpiringWithinDays(days).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/premium-summary")
    public ResponseEntity<Hashtable<VehicleType, Double>> viewPremiumSummaryByVehicleType() {
        Hashtable<VehicleType, Double> summary = policyRegisterService.totalPremiumByVehicleType();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/count-by-vehicle-type")
    public ResponseEntity<Hashtable<VehicleType, Integer>> viewNumberOfPoliciesByVehicleType() {
        Hashtable<VehicleType, Integer> counts = policyRegisterService.countPoliciesByVehicleType();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/customer-with-most-policies")
    public ResponseEntity<String> viewCustomerWithMostPolicies() {
        Optional<String> customer = policyRegisterService.getCustomerWithMostPolicies();
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/top-premium")
    public ResponseEntity<Map<String, Double>> viewTop5PoliciesByPremium() {
        Map<String, Double> topPolicies = policyRegisterService.getTop5PoliciesByPremium();
        return ResponseEntity.ok(topPolicies);
    }


    private static LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(date, formatter);
    }


}