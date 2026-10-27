package controller;

import com.wipfli.training.dto.APIStatus;
import com.wipfli.training.dto.PolicyResponse;
import com.wipfli.training.model.PolicyStatus;
import com.wipfli.training.model.VehicleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PolicyControllerApiStatusTest {

    @Test
    void policyResponse_containsApiStatusMetadata() {
        PolicyResponse response = new PolicyResponse(
                "POL-2004",
                PolicyStatus.ACTIVE,
                VehicleType.CAR,
                0,
                "Ajay Verma",
                LocalDate.of(2027, 1, 10),
                new APIStatus(200, true, "Policy created successfully")
        );

        Assertions.assertEquals("POL-2004", response.policyNumber());
        Assertions.assertEquals(new APIStatus(200, true, "Policy created successfully"), response.apiStatus());
        Assertions.assertEquals(200, response.apiStatus().statusCode());
        Assertions.assertTrue(response.apiStatus().success());
        Assertions.assertEquals("Policy created successfully", response.apiStatus().message());
    }
}
