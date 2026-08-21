package service;

import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.service.PolicyValidator;
import fixtures.TestFixtures;
import org.junit.jupiter.api.*;


/**
 * This is Test class targeting the {@link com.wipfli.training.service.PolicyValidator} class.
 * There could be following possible outputs provided by the validate() function of the PolicyValidator class:
 * <ol>
 * <li>Policy can be NULL : {@link com.wipfli.training.exception.PolicyNotFoundException} is thrown</li>
 * <li>Invalid Vehicle Type Provided/ Claims Count is Negative: {@link com.wipfli.training.exception.InvalidPolicyDataException} is thrown</li>
 * <li>Valid Policy Object is passed. In this case, the function executes without throwing any exception or error.</li>
 * </ol>
 *
 */

public class PolicyValidatorTest {
    private PolicyValidator POLICY_VALIDATOR;

    /*
        @BeforeEach annotation in JUnit 5 is used to mark a method that should execute before each test method.
        It helps in providing initialization or setting up some common test fixtures required.
     */

    @BeforeEach
    void setUp(){
        POLICY_VALIDATOR = new PolicyValidator();
    }

    @Test
    @DisplayName("Test case to validate the PolicyValidator.validate() function when a NULL policy object is passed")
    void policyValidator_validate_returnsPolicyNotFoundException(){
        Assertions.assertThrows(PolicyNotFoundException.class, () -> POLICY_VALIDATOR.validate(null));
    }

    @Test
    @DisplayName("Test case to validate the PolicyValidator.validate() function when a Policy object with Invalid data is passed.")
    void policyValidator_validate_returnsPolicyBusinessException(){
        Assertions.assertThrows(InvalidPolicyDataException.class, () -> POLICY_VALIDATOR.validate(TestFixtures.carPolicyWithNegativeClaimsCount())
        );

    }

    @Test
    @DisplayName("Test case to validate the PolicyValidator.validate() function when a valid Policy object is passed.")
    void policyValidator_validate_returnsNoException() {
        // Since validate() method has void return type, upon successful completion without any exceptions
        // it must not throw any exception. To check the same, assertDoesNotThrow() function is being used here.

        Assertions.assertDoesNotThrow(() -> POLICY_VALIDATOR.validate(TestFixtures.pol2001()
        ));
    }

}
