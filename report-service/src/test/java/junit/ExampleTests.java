package junit;

import io.quarkus.test.junit.QuarkusTest;
import messaging.Event;
import messaging.EventSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ReportService;
import services.interfaces.IReportService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleTests {

    @BeforeEach
    public void setUp() {
        System.out.println("Setting up...");
    }

    @AfterEach
    public void tearDown() {
        // Put teardown code here if needed.
        System.out.println("Tearing down...");
    }

}
