package com.wipfli.training.model;

import com.wipfli.training.dto.APIStatus;
import com.wipfli.training.dto.PolicyRequest;
import com.wipfli.training.dto.PolicyResponse;
import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.repository.VehicleTypeCount;
import com.wipfli.training.service.PolicyFactory;
import com.wipfli.training.service.PolicyRegister;
import com.wipfli.training.service.PolicyValidator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/policies")
@Transactional
public class PolicyController {

    private final PolicyRegister policyRegisterService;

    public PolicyController(PolicyRegister policyRegisterService) {
        this.policyRegisterService = policyRegisterService;
    }

    @PostMapping
    public ResponseEntity<PolicyResponse> addPolicy(@RequestBody PolicyRequest request) throws PolicyNotFoundException {

        // Check for duplicate policy number
        if (policyRegisterService.findByNumber(request.policyNumber()).isPresent()) {
            throw new DuplicatePolicyNumberException(
                    request.policyNumber(),
                    "A policy with number " + request.policyNumber() + " already exists"
            );
        }

        PolicyHolder holder = new PolicyHolder(
                request.holderFirstName(),
                request.holderLastName(),
                request.holderAge(),
                request.holderState()
        );

        PolicyValidator.validateAge(request.holderAge());

        LocalDate expiryDate = parseDate(request.expiryDate());
        LocalDate startDate = parseDate(request.startDate());

        Policy policy = switch (request.vehicleType()) {
            case CAR -> PolicyFactory.createCarPolicy(
                    request.policyNumber(),
                    holder,
                    request.registrationNumber(),
                    request.claimsCount(),
                    startDate,
                    expiryDate
            );
            case BIKE -> PolicyFactory.createBikePolicy(
                    request.policyNumber(),
                    holder,
                    request.engineCC(),
                    request.claimsCount(),
                    startDate,
                    expiryDate
            );
            case TRUCK -> PolicyFactory.createTruckPolicy(
                    request.policyNumber(),
                    holder,
                    request.loadCapacity(),
                    request.claimsCount(),
                    startDate,
                    expiryDate
            );
        };

        PolicyValidator.validate(policy);

        System.out.println("Adding policy: " + policy + " for customer: " + holder.getDisplayName());

        policyRegisterService.add(policy);

        return ResponseEntity.status(HttpStatus.CREATED).body(PolicyResponse.created(
                policy.getPolicyNumber(),
                policy.getPolicyStatus(),
                policy.getVehicleType(),
                policy.getClaimsCount(),
                policy.getPolicyHolder().getDisplayName(),
                policy.getExpiryDate()
        ));
    }

    @GetMapping("/{policyNumber}")
    public ResponseEntity<PolicyResponse> viewPolicyDetails(@PathVariable String policyNumber) throws PolicyNotFoundException {
        Policy policy = policyRegisterService.findByNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException(policyNumber, "No policy found with number: " + policyNumber));

        return ResponseEntity.ok(PolicyResponse.retrieved(
                policy.getPolicyNumber(),
                policy.getPolicyStatus(),
                policy.getVehicleType(),
                policy.getClaimsCount(),
                policy.getPolicyHolder().getDisplayName(),
                policy.getExpiryDate()
        ));
    }

    @GetMapping("/{policyNumber}/premium")
    public ResponseEntity<Map<String, Object>> getPremium(@PathVariable String policyNumber)
            throws PolicyNotFoundException {

        Policy policy = policyRegisterService.getExistingPolicy(policyNumber);
        double premium = policyRegisterService.getPremium(policyNumber);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("policyNumber", policy.getPolicyNumber());
        response.put("vehicleType", policy.getVehicleType());
        response.put("premium", premium);
        response.put("currency", "INR");
        response.put("holderName", policy.getPolicyHolder().getDisplayName());
        response.put("holderAge", policy.getPolicyHolder().getAge());
        response.put("claimsCount", policy.getClaimsCount());
        response.put("status", Map.of(
                "code", HttpStatus.OK.value(),
                "success", true,
                "message", "Premium calculated successfully",
                "timestamp", java.time.LocalDateTime.now()
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<Map<String, Object>> viewPoliciesForCustomer(@PathVariable String customerName) {
        List<Policy> policies = policyRegisterService.findByCustomer(customerName);
        List<PolicyResponse> responses = policies.stream()
                .map(policy -> PolicyResponse.retrieved(
                        policy.getPolicyNumber(),
                        policy.getPolicyStatus(),
                        policy.getVehicleType(),
                        policy.getClaimsCount(),
                        policy.getPolicyHolder().getDisplayName(),
                        policy.getExpiryDate()
                ))
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", responses);
        response.put("count", responses.size());
        response.put("status", Map.of(
                "code", HttpStatus.OK.value(),
                "success", true,
                "message", responses.isEmpty()
                    ? "No policies found for customer: " + customerName
                    : "Retrieved " + responses.size() + " policy/policies for customer: " + customerName,
                "timestamp", java.time.LocalDateTime.now()
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{firstName}/{lastName}")
    public ResponseEntity<Map<String, Object>> viewPoliciesForCustomerByFullName(
            @PathVariable String firstName,
            @PathVariable String lastName) {

        List<Policy> policies = policyRegisterService.findByCustomerFullName(firstName, lastName);
        String customerName = firstName + " " + lastName;

        List<PolicyResponse> responses = policies.stream()
                .map(policy -> PolicyResponse.retrieved(
                        policy.getPolicyNumber(),
                        policy.getPolicyStatus(),
                        policy.getVehicleType(),
                        policy.getClaimsCount(),
                        policy.getPolicyHolder().getDisplayName(),
                        policy.getExpiryDate()
                ))
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", responses);
        response.put("count", responses.size());
        response.put("status", Map.of(
                "code", HttpStatus.OK.value(),
                "success", true,
                "message", responses.isEmpty()
                        ? "No policies found for customer: " + customerName
                        : "Retrieved " + responses.size() + " policy/policies for customer: " + customerName,
                "timestamp", java.time.LocalDateTime.now()
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehicle-type/{vehicleType}")
    public ResponseEntity<Map<String, Object>> viewPoliciesByVehicleType(@PathVariable VehicleType vehicleType) {
        List<Policy> policies = policyRegisterService.findByVehicleType(vehicleType);
        List<PolicyResponse> responses = policies.stream()
                .map(policy -> PolicyResponse.retrieved(
                        policy.getPolicyNumber(),
                        policy.getPolicyStatus(),
                        policy.getVehicleType(),
                        policy.getClaimsCount(),
                        policy.getPolicyHolder().getDisplayName(),
                        policy.getExpiryDate()
                ))
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", responses);
        response.put("count", responses.size());
        response.put("vehicleType", vehicleType);
        response.put("status", Map.of(
                "code", HttpStatus.OK.value(),
                "success", true,
                "message", responses.isEmpty()
                    ? "No policies found for vehicle type: " + vehicleType
                    : "Retrieved " + responses.size() + " policy/policies of type: " + vehicleType,
                "timestamp", java.time.LocalDateTime.now()
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/expiring")
    public ResponseEntity<Map<String, Object>> viewPoliciesExpiringSoon(@RequestParam int days) {

        if(days < 0){
            return ResponseEntity.badRequest().body(Map.of(
                "status", Map.of(
                    "code", HttpStatus.BAD_REQUEST.value(),
                    "success", false,
                    "message", "Days parameter must be non-negative",
                    "timestamp", java.time.LocalDateTime.now()
                )
            ));
        }

        List<Policy> policies = policyRegisterService.findExpiringWithinDays(days);
        List<PolicyResponse> responses = policies.stream()
                .map(policy -> PolicyResponse.retrieved(
                        policy.getPolicyNumber(),
                        policy.getPolicyStatus(),
                        policy.getVehicleType(),
                        policy.getClaimsCount(),
                        policy.getPolicyHolder().getDisplayName(),
                        policy.getExpiryDate()
                ))
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", responses);
        response.put("count", responses.size());
        response.put("expiringWithinDays", days);
        response.put("status", Map.of(
                "code", HttpStatus.OK.value(),
                "success", true,
                "message", responses.isEmpty()
                    ? "No policies expiring within " + days + " days"
                    : "Found " + responses.size() + " policy/policies expiring within " + days + " days",
                "timestamp", java.time.LocalDateTime.now()
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/premium-summary")
    public ResponseEntity<Map<String, Object>> viewPremiumSummaryByVehicleType() {
        Map<VehicleType, Double> summary = policyRegisterService.totalPremiumByVehicleType();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("summary", summary);
        response.put("totalPremium", summary.values().stream().mapToDouble(Double::doubleValue).sum());
        response.put("currency", "INR");
        response.put("status", Map.of(
                "code", HttpStatus.OK.value(),
                "success", true,
                "message", "Premium summary calculated for all vehicle types",
                "timestamp", java.time.LocalDateTime.now()
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count-by-vehicle-type")
    public ResponseEntity<Map<String, Object>> viewNumberOfPoliciesByVehicleType() {
        List<VehicleTypeCount> counts = policyRegisterService.countPoliciesByVehicleType();
        long totalPolicies = counts.stream().mapToLong(VehicleTypeCount::policyCount).sum();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", counts);
        response.put("totalPolicies", totalPolicies);
        response.put("status", Map.of(
                "code", HttpStatus.OK.value(),
                "success", true,
                "message", "Retrieved policy count by vehicle type: " + counts.size() + " vehicle type(s)",
                "timestamp", java.time.LocalDateTime.now()
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer-with-most-policies")
    public ResponseEntity<Map<String, Object>> viewCustomerWithMostPolicies() {
        Optional<String> customer = policyRegisterService.getCustomerWithMostPolicies();

        if (customer.isEmpty()) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("data", null);
            response.put("status", Map.of(
                    "code", HttpStatus.NOT_FOUND.value(),
                    "success", false,
                    "message", "No customer data available",
                    "timestamp", java.time.LocalDateTime.now()
            ));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String[] parts = customer.get().split(",");
        String fullName = parts[0] + " " + parts[1];
        long count = Long.parseLong(parts[2]);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("customerName", fullName);
        response.put("policyCount", count);
        response.put("status", Map.of(
                "code", HttpStatus.OK.value(),
                "success", true,
                "message", "Customer with most policies: " + fullName + " with " + count + " policy/policies",
                "timestamp", java.time.LocalDateTime.now()
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-premium")
    public ResponseEntity<Map<String, Object>> viewTop5PoliciesByPremium() {
        Map<String, Double> topPolicies = policyRegisterService.getTop5PoliciesByPremium();
        double totalTopPremium = topPolicies.values().stream().mapToDouble(Double::doubleValue).sum();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", topPolicies);
        response.put("count", topPolicies.size());
        response.put("totalPremium", totalTopPremium);
        response.put("currency", "INR");
        response.put("status", Map.of(
                "code", HttpStatus.OK.value(),
                "success", true,
                "message", topPolicies.isEmpty()
                    ? "No policies available"
                    : "Retrieved top " + topPolicies.size() + " policy/policies by premium",
                "timestamp", java.time.LocalDateTime.now()
        ));

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{policyNumber}/expire")
    public ResponseEntity<PolicyResponse> expire(@PathVariable String policyNumber) throws PolicyNotFoundException {
        policyRegisterService.expire(policyNumber);
        Policy policy = policyRegisterService.getExistingPolicy(policyNumber);
        return ResponseEntity.ok(PolicyResponse.updated(
                policy.getPolicyNumber(),
                policy.getPolicyStatus(),
                policy.getVehicleType(),
                policy.getClaimsCount(),
                policy.getPolicyHolder().getDisplayName(),
                policy.getExpiryDate(),
                "Policy " + policyNumber + " has been expired successfully"
        ));
    }

    @PatchMapping("/{policyNumber}/renew")
    public ResponseEntity<PolicyResponse> renew(@PathVariable String policyNumber,
                                                @RequestParam("on") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate renewalDate
    ) throws PolicyNotFoundException {
        policyRegisterService.renew(policyNumber, renewalDate);
        Policy policy = policyRegisterService.getExistingPolicy(policyNumber);
        return ResponseEntity.ok(PolicyResponse.updated(
                policy.getPolicyNumber(),
                policy.getPolicyStatus(),
                policy.getVehicleType(),
                policy.getClaimsCount(),
                policy.getPolicyHolder().getDisplayName(),
                policy.getExpiryDate(),
                "Policy " + policyNumber + " has been renewed successfully until " + renewalDate
        ));
    }

    @PatchMapping("/{policyNumber}/recordClaims")
    public ResponseEntity<PolicyResponse> recordClaims(@PathVariable String policyNumber,
                                                       @RequestParam("claims") int claims
    ) throws PolicyNotFoundException {

        for(int i = 0; i < claims; i++) {
            policyRegisterService.recordClaim(policyNumber);
        }

        Policy policy = policyRegisterService.getExistingPolicy(policyNumber);
        return ResponseEntity.ok(PolicyResponse.updated(
                policy.getPolicyNumber(),
                policy.getPolicyStatus(),
                policy.getVehicleType(),
                policy.getClaimsCount(),
                policy.getPolicyHolder().getDisplayName(),
                policy.getExpiryDate(),
                "Recorded " + claims + " claim(s) for policy " + policyNumber + ". Total claims: " + policy.getClaimsCount()
        ));
    }

    private static LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(date, formatter);
    }


}