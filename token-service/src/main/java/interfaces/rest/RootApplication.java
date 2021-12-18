/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package interfaces.rest;

import services.TokenEventService;
import interfaces.rabbitmq.TokenListener;
import interfaces.rabbitmq.TokenSender;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import services.TokenService;

import javax.enterprise.event.Observes;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.logging.Level;
import java.util.logging.Logger;

@OpenAPIDefinition(
        info = @Info(
                title = "Group 11 - token-service API",
                version = "3.0.3"
        ))
@ApplicationPath("/api/v1")
public class RootApplication extends Application {

    private final static Logger LOGGER = Logger.getLogger(RootApplication.class.getName());

    public RootApplication() {
        super();
    }

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        try {
            TokenSender sender = new TokenSender();
            sender.initConnection();
            TokenEventService tokenEventService = new TokenEventService(sender, new TokenService());
            TokenListener receiver = new TokenListener(tokenEventService);
            receiver.initConnection();
            LOGGER.log(Level.INFO, "RabbitMQ initConnection() started successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "RabbitMQ initConnection() failed: " + e.getMessage());
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

}
