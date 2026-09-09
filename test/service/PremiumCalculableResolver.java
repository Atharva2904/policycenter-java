package service;

import com.wipfli.training.model.PremiumCalculable;
import com.wipfli.training.service.StandardPremiumCalculator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * This is a custom parameter resolver for JUnit 5 that provides an
 * instance of StandardPremiumCalculator whenever a test method requires a parameter of type PremiumCalculable.
 */
public class PremiumCalculableResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == PremiumCalculable.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new StandardPremiumCalculator();
    }
}
