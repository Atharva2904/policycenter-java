package exception;

import com.wipfli.training.dto.ErrorResponse;
import com.wipfli.training.exception.GlobalExceptionHandler;
import com.wipfli.training.exception.PolicyNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;


public class GlobalExceptionHandlerApiStatusTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void errorResponse_containsApiStatusMetadata() {
        ErrorResponse response = new ErrorResponse(
                "POL-404",
                "No policy found with number: POL-404",
                Timestamp.from(Instant.now()),
                com.wipfli.training.dto.APIStatus.failure(404, "No policy found with number: POL-404")
        );

        Assertions.assertEquals("POL-404", response.policyNumber());
        Assertions.assertEquals("No policy found with number: POL-404", response.message());
        Assertions.assertEquals(404, response.apiStatus().statusCode());
        Assertions.assertFalse(response.apiStatus().success());
        Assertions.assertEquals("No policy found with number: POL-404", response.apiStatus().message());
    }

    @Test
    void handler_notFoundAddsFailureApiStatus() {
        var response = handler.handleNotFound(new PolicyNotFoundException("POL-404", "No policy found with number: POL-404"));

        Assertions.assertEquals(404, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().apiStatus());
        Assertions.assertEquals(404, response.getBody().apiStatus().statusCode());
        Assertions.assertFalse(response.getBody().apiStatus().success());
    }
}

