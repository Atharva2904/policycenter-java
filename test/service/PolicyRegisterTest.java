package service;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.service.PolicyRegister;
import fixtures.TestFixtures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PolicyRegisterTest {
    private PolicyRegister POLICY_REGISTER;

    @BeforeEach
    void setUp(){
        POLICY_REGISTER = new PolicyRegister();
        POLICY_REGISTER.add(TestFixtures.pol2001());
        POLICY_REGISTER.add(TestFixtures.pol2002());
        POLICY_REGISTER.add(TestFixtures.pol2003());
        POLICY_REGISTER.add(TestFixtures.pol2004());
        POLICY_REGISTER.add(TestFixtures.pol2005());
        POLICY_REGISTER.add(TestFixtures.pol2006());
    }

    @Test
    void policyRegister_addNullPolicy_throwsInvalidPolicyDataException(){
        Assertions.assertThrows(InvalidPolicyDataException.class, () -> POLICY_REGISTER.add(null));
    }

    @Test
    void policyRegister_addDuplicatePolicy_throwsDuplicatePolicyNumberException(){
        // Adding same policy twice to the Policy Register to check if it throws DuplicatePolicyNumberException
        Policy policy = TestFixtures.pol2001();
        Assertions.assertThrows(DuplicatePolicyNumberException.class, () ->
            POLICY_REGISTER.add(policy)
    );
    }

    /**
     * With {@link java.util.Optional} we need to test for two paths:
     * The success path where the object is present and the empty path where object is missing
     */
    @Test
    void policyRegister_findPolicyByNumber_returnsPresentOptional(){
        Policy policy = TestFixtures.pol2001();

        Optional<Policy> result = POLICY_REGISTER.findByNumber(policy.getPolicyNumber());
        Assertions.assertTrue(result.isPresent(), "Optional object must not be empty!");

        // Checks if the correct Policy object is returned.
        Assertions.assertEquals(policy, result.get());
    }

    @Test
    void policyRegister_findPolicyByNumber_returnsEmptyOptional(){

        // This is a non-existent policy number.
        // Thus, the method must return an empty optional object
        Optional<Policy> result = POLICY_REGISTER.findByNumber("POL-9999");

        Assertions.assertTrue(result.isEmpty(), "Optional object must be empty for non-existent policy number!");
    }
    
    @Test
    void policyRegister_totalPremiumByVehicleType_returnsCorrectTotalPremium(){
        /*
            Since an EnumMap is being used for storing policies by vehicle type,
            the total premium per vehicle we receive will have keys in the same order as the enum declaration order in VehicleType.java
            We can verify the same using following test case alongside the premium value.
         */

        Map<VehicleType, Double> result = POLICY_REGISTER.totalPremiumByVehicleType();

        // Expected total premium values for each vehicle type based on the policies added above.
        Map<VehicleType, Double> expected = Map.of(
                VehicleType.CAR, 2250.0,
                VehicleType.BIKE, 870.0,
                VehicleType.TRUCK, 864.0
        );

        Assertions.assertEquals(expected, result, "Total premium by vehicle type does not match expected values.");
    }

    @Test
    void policyRegister_findCustomerWithMostPolicies_returnsPresentOptional(){
        Optional<String> result = POLICY_REGISTER.getCustomerWithMostPolicies();

        Assertions.assertTrue(result.isPresent(), "Optional object must not be empty!");
        Assertions.assertEquals("Ravi Kumar", result.get(), "Customer with most policies does not match the expected outcome!");
    }

    @Test
    void policyRegister_findCustomerWithMostPolicies_returnsEmptyOptional(){
        // Here, I am resetting the Policy Register object to empty state
        // This way, it should return an empty Optional object since there are no customers in the register.

        POLICY_REGISTER = new PolicyRegister();
        Optional<String> result = POLICY_REGISTER.getCustomerWithMostPolicies();

        Assertions.assertTrue(result.isEmpty(), "Optional Object must be empty if no customer found with most policies!");
    }

    @Test
    void policyRegister_getTop5PoliciesByPremium_returnsPoliciesInDescendingPremiumOrder(){
        Map<String, Double> result = POLICY_REGISTER.getTop5PoliciesByPremium();

        Assertions.assertEquals(5, result.size(), "Top 5 policies by premium should return exactly 5 entries.");

        List<Double> premiums = new ArrayList<>(result.values());
        // Checking for descending order of premium values
        for(int i = 0; i < result.size() - 1; i++){
            Assertions.assertTrue(premiums.get(i) >= premiums.get(i + 1), "Premium values must be in descending order!");
        }

    }

    @Test
    void policyRegister_getTop5PoliciesByPremium_returnsPoliciesCorrectDescendingPremiumOrderValues(){
        Map<String, Double> result = POLICY_REGISTER.getTop5PoliciesByPremium();
        Map<String, Double> expected = Map.of(
                "POL-2006", 1050.0,
                "POL-2002", 864.0,
                "POL-2001", 750.0,
                "POL-2003", 600.0,
                "POL-2004", 450.0
                );

        Assertions.assertEquals(expected, result, "Top 5 policies by premium do not match the expected values.");
    }

    @Test
    void policyRegister_findPoliciesExpiringWithin30Days_returnsCorrectPolicies(){
        List<Policy> result = POLICY_REGISTER.findExpiringWithinDays(30);
        List<Policy> expected = List.of(
                TestFixtures.pol2001(),
                TestFixtures.pol2003(),
                TestFixtures.pol2005(),
                TestFixtures.pol2002()
        );

        Assertions.assertEquals(expected, result, "Policies expiring within 30 days do not match the expected list.");
    }
}
