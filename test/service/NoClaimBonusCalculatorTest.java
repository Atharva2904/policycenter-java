package service;

import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PremiumCalculable;
import com.wipfli.training.service.NoClaimBonusCalculator;
import com.wipfli.training.service.StandardPremiumCalculator;
import fixtures.TestFixtures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(PremiumCalculableResolver.class)
public class NoClaimBonusCalculatorTest {
    private NoClaimBonusCalculator NO_CLAIM_BONUS_CALCULATOR;

    @BeforeEach
    void setUp(PremiumCalculable premiumCalculator){
        NO_CLAIM_BONUS_CALCULATOR = new NoClaimBonusCalculator(premiumCalculator);
    }

    @Test
    void carPolicy_noClaimBonusCalculator_returnsBasePlusSurchargeAndClaimsWithNoClaimBonus() {
        Policy policy = TestFixtures.pol2004();
        double premium = NO_CLAIM_BONUS_CALCULATOR.calculatePremium(policy);

        // Base Premium = 500.0, Age Surcharge = 500.0 * 0.0 = 0.0, Claims Cost = 150.0 * 0 = 0.0
        // Total Premium = 500.0 + 0.0 + 0.0 = 500.0
        // No Claim Bonus = 500.0 * 0.1 = 50.0
        // Final Premium = 500.0 - 50.0 = 450.0

        Assertions.assertEquals(450.0, premium, 0.01);
    }

    @Test
    void truckPolicy_noClaimBonusCalculator_returnsBasePlusSurchargeAndClaimsWithNoClaimBonus() {
        Policy policy = TestFixtures.pol2002();
        double premium = NO_CLAIM_BONUS_CALCULATOR.calculatePremium(policy);

        // Base Premium = 800.0, Age Surcharge = 800.0 * 0.2 = 160.0, Claims Cost = 150.0 * 0 = 0.0
        // Total Premium = 800.0 + 160.0 + 0.0 = 960.0
        // No Claim Bonus = 960.0 * 0.1 = 96.0
        // Final Premium = 960.0 - 96.0 = 864.0

        Assertions.assertEquals(864.0, premium, 0.01);
    }

    @Test
    void bikePolicy_noClaimBonusCalculator_returnsBasePlusSurchargeAndClaimsWithNoClaimBonus() {
        Policy policy = TestFixtures.pol2005();
        double premium = NO_CLAIM_BONUS_CALCULATOR.calculatePremium(policy);

        // Base Premium = 300.0, Age Surcharge = 300.0 * 0.0 = 0.0, Claims Cost = 150.0 * 0 = 0.0
        // Total Premium = 300.0 + 0.0 + 0.0 = 300.0
        // No Claim Bonus = 300.0 * 0.1 = 30.0
        // Final Premium = 300.0 - 30.0 = 270.0

        Assertions.assertEquals(270.0, premium, 0.01);
    }
}
