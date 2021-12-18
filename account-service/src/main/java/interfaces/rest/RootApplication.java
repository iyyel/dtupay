package interfaces.rest;

import interfaces.rabbitmq.AccountFactory;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.enterprise.event.Observes;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.logging.Logger;

@OpenAPIDefinition(
        info = @Info(
                title = "Group 11 - account-service API",
                version = "3.0.3"
        ))
@ApplicationPath("/api/v1")
public class RootApplication extends Application {

    private final static Logger LOGGER = Logger.getLogger(RootApplication.class.getName());

    public RootApplication() {
        super();
    }

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting... RabbitMQ host: " + System.getenv("RABBITMQ_HOST"));
        try {
            LOGGER.info("Starting RabbitMQ AccountEventService...");
            new AccountFactory().getService();
        } catch (Exception e) {
            LOGGER.severe("Failed to start RabbitMQ AccountEventService...");
            e.printStackTrace();
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}