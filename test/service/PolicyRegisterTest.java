package service;

import com.wipfli.training.PolicyCenterApplication;
import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.repository.PolicyRepository;
import com.wipfli.training.service.NoClaimBonusCalculator;
import com.wipfli.training.service.PolicyRegister;
import com.wipfli.training.service.PremiumCalculatorResolver;
import com.wipfli.training.service.StandardPremiumCalculator;
import com.wipfli.training.store.AgeGroupFactorStore;
import fixtures.TestFixtures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

@DataJpaTest
@ContextConfiguration(classes = {
        PolicyCenterApplication.class,
        PolicyRegister.class,
        PolicyRepository.class,
        PremiumCalculatorResolver.class,
        StandardPremiumCalculator.class,
        NoClaimBonusCalculator.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PolicyRegisterTest {

    @Autowired
    private PolicyRegister POLICY_REGISTER;

    @Autowired
    private PolicyRepository POLICY_REPOSITORY;

    @BeforeEach
    void setUp(){
        StandardPremiumCalculator standardPremiumCalculator = new StandardPremiumCalculator(new AgeGroupFactorStore());
        NoClaimBonusCalculator noClaimBonusCalculator = new NoClaimBonusCalculator(standardPremiumCalculator);
        PremiumCalculatorResolver premiumCalculatorResolver = new PremiumCalculatorResolver(
                standardPremiumCalculator,
                noClaimBonusCalculator
        );

        POLICY_REGISTER = new PolicyRegister(premiumCalculatorResolver, POLICY_REPOSITORY);
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
    void policyRegister_getPremium_whenPolicyNotFound_throwsPolicyNotFoundException() {
        Assertions.assertThrows(PolicyNotFoundException.class, () -> POLICY_REGISTER.getPremium("POL-9999"));
    }

    @Test
    void policyRegister_getPremium_usesResolvedStrategy() throws PolicyNotFoundException {
        Assertions.assertEquals(450.0, POLICY_REGISTER.getPremium("POL-2004"), 0.01);
        Assertions.assertEquals(750.0, POLICY_REGISTER.getPremium("POL-2001"), 0.01);
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

        String[] parts = result.get().split(",");
        String fullName = parts[0] + " " + parts[1];
        long count = Long.parseLong(parts[2]);
        Assertions.assertEquals("Ravi Kumar", fullName, "Customer with most policies does not match the expected outcome!");
        Assertions.assertEquals(1, count, "Count of policies for the customer with most policies does not match the expected outcome!");
    }

    @Test
    void policyRegister_findCustomerWithMostPolicies_returnsEmptyOptional(){
        // Here, I am resetting the Policy Register object to empty state
        // This way, it should return an empty Optional object since there are no customers in the register.

        StandardPremiumCalculator standardPremiumCalculator = new StandardPremiumCalculator(new AgeGroupFactorStore());
        NoClaimBonusCalculator noClaimBonusCalculator = new NoClaimBonusCalculator(standardPremiumCalculator);
        PremiumCalculatorResolver premiumCalculatorResolver = new PremiumCalculatorResolver(
                standardPremiumCalculator,
                noClaimBonusCalculator
        );

        POLICY_REGISTER = new PolicyRegister(premiumCalculatorResolver, POLICY_REPOSITORY);
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
        Comparator<Policy> policyComparator = Comparator.comparing(Policy::getPolicyNumber);
        List<Policy> result = POLICY_REGISTER.findExpiringWithinDays(30);
        List<Policy> expected = new ArrayList<>(List.of(
                TestFixtures.pol2001(),
                TestFixtures.pol2003(),
                TestFixtures.pol2005(),
                TestFixtures.pol2002()
        ));

        result.sort(policyComparator);
        expected.sort(policyComparator);


        Assertions.assertEquals(expected, result, "Policies expiring within 30 days do not match the expected list.");
    }
}
