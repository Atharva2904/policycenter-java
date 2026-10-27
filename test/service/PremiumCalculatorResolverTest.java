package service;

import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PremiumCalculable;
import com.wipfli.training.service.NoClaimBonusCalculator;
import com.wipfli.training.service.PremiumCalculatorResolver;
import com.wipfli.training.service.StandardPremiumCalculator;
import com.wipfli.training.store.AgeGroupFactorStore;
import fixtures.TestFixtures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PremiumCalculatorResolverTest {

    private final StandardPremiumCalculator standardPremiumCalculator =
            new StandardPremiumCalculator(new AgeGroupFactorStore());
    private final NoClaimBonusCalculator noClaimBonusCalculator =
            new NoClaimBonusCalculator(standardPremiumCalculator);
    private final PremiumCalculatorResolver resolver =
            new PremiumCalculatorResolver(standardPremiumCalculator, noClaimBonusCalculator);

    @Test
    void resolve_zeroClaims_selectsNoClaimBonusCalculator() {
        Policy policy = TestFixtures.pol2004();

        PremiumCalculable selected = resolver.resolve(policy);

        Assertions.assertSame(noClaimBonusCalculator, selected);
    }

    @Test
    void resolve_oneOrMoreClaims_selectsStandardCalculator() {
        Policy policy = TestFixtures.pol2001();

        PremiumCalculable selected = resolver.resolve(policy);

        Assertions.assertSame(standardPremiumCalculator, selected);
    }

    @Test
    void noClaimBonusCalculator_delegatesToStandardCalculatorAndAppliesBonusExactlyOnce() {
        Policy policy = TestFixtures.pol2004();

        double expected = standardPremiumCalculator.calculatePremium(policy) * 0.9;
        double actual = noClaimBonusCalculator.calculatePremium(policy);

        Assertions.assertEquals(expected, actual, 0.01);
    }

    @Test
    void resolve_nullPolicy_throwsIllegalArgumentException() {
        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(null)
        );

        Assertions.assertTrue(ex.getMessage().contains("Policy cannot be null"));
    }

    @Test
    void resolve_incompletePolicyData_throwsIllegalArgumentException() {
        Policy incompletePolicy = new com.wipfli.training.model.CarPolicy(
                "POL-INVALID",
                null,
                "REG-XYZ",
                0,
                java.time.LocalDate.of(2027, 1, 10)
        );

        IllegalArgumentException ex = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(incompletePolicy)
        );

        Assertions.assertTrue(ex.getMessage().contains("Policy holder cannot be null"));
    }
}

