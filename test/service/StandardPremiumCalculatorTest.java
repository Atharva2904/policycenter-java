package service;

import com.wipfli.training.model.Policy;
import com.wipfli.training.service.StandardPremiumCalculator;
import fixtures.TestFixtures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StandardPremiumCalculatorTest {
    private StandardPremiumCalculator STANDARD_PREMIUM_CALCULATOR;

    @BeforeEach
    void setUp(){
        STANDARD_PREMIUM_CALCULATOR = new StandardPremiumCalculator();
    }


    @Test
    @DisplayName("CarPolicy: Test case to validate the premium value returned by the StandardCalculator.calculatePremium() function against expected value.")
    void test_carPolicy_standardCalculator_returnsBasePlusSurchargeAndClaims(){
        Policy policy = TestFixtures.pol2001();
        double premium = STANDARD_PREMIUM_CALCULATOR.calculatePremium(policy);
        // Base Premium = 500.0, Age Surcharge = 500.0 * 0.2 = 100.0, Claims Cost = 150.0 * 1 = 150.0
        // Total Premium = 500.0 + 100.0 + 150.0 = 750.0

        Assertions.assertEquals(750.0, premium);
    }

    @Test
    @DisplayName("TruckPolicy: Test case to validate the premium value returned by the StandardCalculator.calculatePremium() function against expected value.")
    void test_truckPolicy_standardCalculator_returnsBasePlusSurchargeAndClaims(){
        Policy policy = TestFixtures.pol2002();
        double premium = STANDARD_PREMIUM_CALCULATOR.calculatePremium(policy);
        // Base Premium = 800.0, Age Surcharge = 800.0 * 0.2 = 160.0, Claims Cost = 150.0 * 0 = 0.0
        // Total Premium = 800.0 + 160.0 + 0.0 = 960.0

        /* Here, a Delta value is passed as third argument to assertEquals()
         * Floating point numbers (e.g. double and float) can have precision errors in Java.
         * To tackle this, a tolerance range: delta value is provided
         * It represents the maximum acceptable absolute difference allowed between expected and actual values.
        */

        Assertions.assertEquals(960.0, premium, 0.01);
    }

    @Test
    @DisplayName("BikePolicy: Test case to validate the premium value returned by the StandardCalculator.calculatePremium() function against expected value.")
    void test_bikePolicy_standardCalculator_returnsBasePlusSurchargeAndClaims(){
        Policy policy = TestFixtures.pol2003();

        double premium = STANDARD_PREMIUM_CALCULATOR.calculatePremium(policy);
        // Base Premium = 300.0, Age Surcharge = 300.0 * 0.0 = 0.0, Claims Cost = 150.0 * 2 = 300.0
        // Total Premium = 300.0 + 0.0 + 300.0 = 600.0

        Assertions.assertEquals(600.0, premium, 0.01);
    }
}
