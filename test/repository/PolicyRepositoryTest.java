package repository;

import com.wipfli.training.PolicyCenterApplication;
import com.wipfli.training.exception.IllegalStatusChangeException;
import com.wipfli.training.exception.RenewalNotAllowedException;
import com.wipfli.training.model.*;
import com.wipfli.training.repository.PolicyRepository;
import fixtures.TestFixtures;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {
        PolicyCenterApplication.class,
        PolicyRepository.class,

})

public class PolicyRepositoryTest {

    @Autowired
    private TestEntityManager TEST_ENTITY_MANAGER;

    @Autowired
    private PolicyRepository POLICY_REPOSITORY;

    @Test
    void save_carPolicy_persistsAndReturnsCarPolicyWithRegistrationNumber(){
        Policy carPolicy = TestFixtures.pol2001();

        POLICY_REPOSITORY.save(carPolicy);


        // Forcing the flush to the database to ensure the entity is persisted
        TEST_ENTITY_MANAGER.flush();

        // Clearing the persistence context so next call is directly made to DB
        TEST_ENTITY_MANAGER.clear();

        Optional<Policy> policyFound = POLICY_REPOSITORY.findByPolicyNumber(carPolicy.getPolicyNumber());

        // Asserting that DB returns Policy Record
        Assertions.assertTrue(policyFound.isPresent(), "Policy should be found in the database");

        // Asserting the class of Policy returned (or type)
        Assertions.assertInstanceOf(CarPolicy.class, policyFound.get(), "The class of the found policy should match the saved policy");


        // Asserting that policy number of Policy returned is matching the intended policy's number
        Assertions.assertEquals(carPolicy.getPolicyNumber(), policyFound.get().getPolicyNumber());

        // Asserting that the registration number of the found policy matches the saved policy's registration number
        Assertions.assertEquals(((CarPolicy)policyFound.get()).getRegistrationNumber(), ((CarPolicy)carPolicy).getRegistrationNumber(), "The registration number of the found policy should match the saved policy's registration number");

    }

    @Test
    void save_BikePolicy_persistsAndReturnsBikePolicyWithEngineCC(){
        Policy bikePolicy = TestFixtures.pol2003();

        POLICY_REPOSITORY.save(bikePolicy);


        // Forcing the flush to the database to ensure the entity is persisted
        TEST_ENTITY_MANAGER.flush();

        // Clearing the persistence context so next call is directly made to DB
        TEST_ENTITY_MANAGER.clear();

        Optional<Policy> policyFound = POLICY_REPOSITORY.findByPolicyNumber(bikePolicy.getPolicyNumber());

        // Asserting that DB returns Policy Record
        Assertions.assertTrue(policyFound.isPresent(), "Policy should be found in the database");

        // Asserting the class of Policy returned (or type)
        Assertions.assertInstanceOf(BikePolicy.class, policyFound.get(), "The class of the found policy should match the saved policy");


        // Asserting that policy number of Policy returned is matching the intended policy's number
        Assertions.assertEquals(bikePolicy.getPolicyNumber(), policyFound.get().getPolicyNumber());

        // Asserting that the registration number of the found policy matches the saved policy's registration number
        Assertions.assertEquals(((BikePolicy)policyFound.get()).getEngineCC(), ((BikePolicy) bikePolicy).getEngineCC(), "The engine CC of the found policy should match the saved policy's engine CC");

    }

    @Test
    void save_TruckPolicy_persistsAndReturnsTruckPolicyWithLoadCapacity(){
        Policy truckPolicy = TestFixtures.pol2002();

        POLICY_REPOSITORY.save(truckPolicy);


        // Forcing the flush to the database to ensure the entity is persisted
        TEST_ENTITY_MANAGER.flush();

        // Clearing the persistence context so next call is directly made to DB
        TEST_ENTITY_MANAGER.clear();

        Optional<Policy> policyFound = POLICY_REPOSITORY.findByPolicyNumber(truckPolicy.getPolicyNumber());

        // Asserting that DB returns Policy Record
        Assertions.assertTrue(policyFound.isPresent(), "Policy should be found in the database");

        // Asserting the class of Policy returned (or type)
        Assertions.assertInstanceOf(TruckPolicy.class, policyFound.get(), "The class of the found policy should match the saved policy");


        // Asserting that policy number of Policy returned is matching the intended policy's number
        Assertions.assertEquals(truckPolicy.getPolicyNumber(), policyFound.get().getPolicyNumber());

        // Asserting that the load capacity of the found policy matches the saved policy's load capacity
        Assertions.assertEquals(((TruckPolicy)policyFound.get()).getLoadCapacityTons(), ((TruckPolicy)truckPolicy).getLoadCapacityTons(), "The load capacity of the found policy should match the saved policy's load capacity");

    }

    @Test
    void policy_statusChange_persistsInTheDatabase(){
        Policy policy = TestFixtures.pol2001();
        POLICY_REPOSITORY.save(policy);

        // Forcing the flush to the database to ensure the entity is persisted
        TEST_ENTITY_MANAGER.flush();

        // Changing the status of the policy
        policy.expire();
        POLICY_REPOSITORY.save(policy);

        // Forcing the flush to the database to ensure the entity is persisted
        TEST_ENTITY_MANAGER.flush();

        // Clearing the persistence context so next call is directly made to DB
        TEST_ENTITY_MANAGER.clear();

        Optional<Policy> policyFound = POLICY_REPOSITORY.findByPolicyNumber(policy.getPolicyNumber());

        // Asserting that DB returns Policy Record
        Assertions.assertTrue(policyFound.isPresent(), "Policy should be found in the database");

        // Asserting that the status of the found policy matches the updated status
        Assertions.assertEquals(PolicyStatus.EXPIRED, policyFound.get().getPolicyStatus(), "The status of the found policy should match the updated status");
    }

    @Test
    void expired_policy_doesNotAllow_Renewal(){
        Policy policy = TestFixtures.pol2001();
        POLICY_REPOSITORY.save(policy);

        // Forcing the flush to the database to ensure the entity is persisted
        TEST_ENTITY_MANAGER.flush();

        // Changing the status of the policy
        policy.expire();
        POLICY_REPOSITORY.save(policy);

        // Forcing the flush to the database to ensure the entity is persisted
        TEST_ENTITY_MANAGER.flush();

        // Clearing the persistence context so next call is directly made to DB
        TEST_ENTITY_MANAGER.clear();

        Optional<Policy> policyFound = POLICY_REPOSITORY.findByPolicyNumber(policy.getPolicyNumber());

        // Asserting that DB returns Policy Record
        Assertions.assertTrue(policyFound.isPresent(), "Policy should be found in the database");

        // Renewing the policy
        Assertions.assertThrows(IllegalStatusChangeException.class, () -> policyFound.get().renew(LocalDate.now()), "Renewal of an expired policy should throw RenewalNotAllowedException");


    }

    @Test
    void status_field_isPersistedCorrectlyInTheDatabase(){
        Policy policy = TestFixtures.pol2001();
        POLICY_REPOSITORY.save(policy);

        // Forcing the flush to the database to ensure the entity is persisted
        TEST_ENTITY_MANAGER.flush();

        // Clearing the persistence context so next call is directly made to DB
        TEST_ENTITY_MANAGER.clear();

        Optional<Policy> policyFound = POLICY_REPOSITORY.findByPolicyNumber(policy.getPolicyNumber());

        // Asserting that DB returns Policy Record
        Assertions.assertTrue(policyFound.isPresent(), "Policy should be found in the database");

        // Asserting that the status of the found policy is in text format

        String rawStatus = TEST_ENTITY_MANAGER.getEntityManager()
                .createNativeQuery("SELECT policy_status FROM policy WHERE policy_number = :num", String.class)
                .setParameter("num", policy.getPolicyNumber())
                .getSingleResult()
                .toString();

        Assertions.assertEquals("ACTIVE", rawStatus, "Status should be stored in text format and not a number");

    }

    @Test
    void customerName_query_returnsPoliciesOfTheSameCustomer(){
        POLICY_REPOSITORY.save(TestFixtures.pol2002());
        POLICY_REPOSITORY.save(TestFixtures.pol2007());

        TEST_ENTITY_MANAGER.flush();

        TEST_ENTITY_MANAGER.clear();

        List<Policy> customerPolicies = POLICY_REPOSITORY.findByPolicyHolderFirstName("Kavya");


        customerPolicies.forEach(
                policy ->
                        Assertions.assertEquals("Kavya", policy.getPolicyHolder().getDisplayName().split(" ")[0], "The first name of the policy holder should match the queried name")
        );
    }

    @Test
    void findByExpiryDate_includesBothBoundaryDates(){
        POLICY_REPOSITORY.save(TestFixtures.pol2006());
        POLICY_REPOSITORY.save(TestFixtures.pol2007());
        POLICY_REPOSITORY.save(TestFixtures.pol2002());
        POLICY_REPOSITORY.save(TestFixtures.pol2001());


        TEST_ENTITY_MANAGER.flush();

        TEST_ENTITY_MANAGER.clear();

        LocalDate start = LocalDate.of(2026, 9, 1);
        LocalDate end = LocalDate.of(2026, 12, 1);

        List<Policy> results = POLICY_REPOSITORY.findByExpiryDateBetween(start, end);

        Assertions.assertTrue(
                results.stream().anyMatch(policy -> policy.getExpiryDate().equals(start)),
                "The results should include policies expiring on the start date"
        );

        Assertions.assertTrue(
                results.stream().anyMatch(policy -> policy.getExpiryDate().equals(end)),
                "The results should include policies expiring on the end date"
        );


    }

    @Test
    void findByExpiryDateBetween_excludesJustOutsideBoundaryRange(){
        POLICY_REPOSITORY.save(TestFixtures.pol2006());
        POLICY_REPOSITORY.save(TestFixtures.pol2007());
        POLICY_REPOSITORY.save(TestFixtures.pol2002());
        POLICY_REPOSITORY.save(TestFixtures.pol2001());


        TEST_ENTITY_MANAGER.flush();

        TEST_ENTITY_MANAGER.clear();

        LocalDate start = LocalDate.of(2026, 8, 31);
        LocalDate end = LocalDate.of(2026, 11, 30);

        List<Policy> results = POLICY_REPOSITORY.findByExpiryDateBetween(start, end);

        Assertions.assertTrue(
                results.stream().noneMatch(policy -> policy.getExpiryDate().equals(start)),
                "The results should not include policies expiring just outside start range date"
        );

        Assertions.assertTrue(
                results.stream().noneMatch(policy -> policy.getExpiryDate().equals(end)),
                "The results should not include policies expiring just outside end range date"
        );
    }



    @Test
    void save_policiesWithSamePolicyNumber(){

        // Two Policy Objects with same policy number

        POLICY_REPOSITORY.save(TestFixtures.pol2007());
        TEST_ENTITY_MANAGER.flush();
        TEST_ENTITY_MANAGER.clear();

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () ->
                    POLICY_REPOSITORY.saveAndFlush(TestFixtures.pol2007())
                );


    }




}
