package fixtures;

import com.wipfli.training.model.*;

import java.time.LocalDate;

/**
 * This class serves as the data source for various unit test cases written for classes in {@link com.wipfli.training.service} package.
 * It is declared as final class with static methods serving as data sources. The methods return various PolicyHolder and Policy objects with different attributes to be used in unit tests.
 * In Java, final class prevents subclassing from it; i.e. it cannot be extended or inherited by any other class.
 * This helps in method inlining. Since, ordinarily, JVM must look up at the methods at the runtime to see if a subclass has overridden a method, but if the class is final,
 * JVM can inline the method calls at compile time, which can improve performance by eliminating the CPU overhead of a method call.
 * The methods inside are declared static since their implementation does not directly alter or modify the state of the TestFixtures class.
 * They are utility methods that provide specific data for testing purposes.
 *
 */

public final class TestFixtures {

    public static PolicyHolder userRavi() {
        return new PolicyHolder(
                "Ravi",
                "Kumar",
                22,
                State.IS
        );
    }

    public static PolicyHolder userMeena() {
        return new PolicyHolder(
                "Meena",
                "Iyer",
                34,
                State.IN
        );
    }

    public static PolicyHolder userAjay() {
        return new PolicyHolder(
                "Ajay",
                "Verma",
                41,
                State.MN
        );
    }

    public static PolicyHolder userSneha() {
        return new PolicyHolder(
                "Sneha",
                "Rao",
                29,
                State.IS
        );
    }



    public static CarPolicy pol2001() {
        return new CarPolicy(
                "POL-2001",
                userRavi(),
                "IL-ABC-123",
                1,
                LocalDate.of(2026, 8, 15)
        );
    }

    public static CarPolicy pol2004() {
        return new CarPolicy(
                "POL-2004",
                userAjay(),
                "IN-XYZ-789",
                0,
                LocalDate.of(2027, 1, 10)
        );
    }

    public static CarPolicy pol2006() {
        return new CarPolicy(
                "POL-2006",
                userRavi(),
                "IL-ABC-123",
                3,
                LocalDate.of(2026, 12, 1)
        );
    }


    public static BikePolicy pol2003() {
        return new BikePolicy(
                "POL-2003",
                userMeena(),
                150,
                2,
                LocalDate.of(2026, 8, 20)
        );
    }

    public static BikePolicy pol2005() {
        return new BikePolicy(
                "POL-2005",
                userSneha(),
                650,
                0,
                LocalDate.of(2026, 8, 25)
        );
    }


    public static TruckPolicy pol2002() {
        return new TruckPolicy(
                "POL-2002",
                userRavi(),
                12.5,
                0,
                LocalDate.of(2026, 9, 1)
        );
    }

    public static CarPolicy carPolicyWithNegativeClaimsCount() {
        return new CarPolicy(
                "POL-2007",
                userAjay(),
                "IN-XYZ-789",
                -1,
                LocalDate.of(2027, 1, 10)
        );
    }

    public static CarPolicy carPolicyWithInvalidVehicleType(){
        return new CarPolicy(
                "POL-2008",
                userAjay(),
                null,
                1,
                LocalDate.of(2027, 1, 10)
        );
    }
}