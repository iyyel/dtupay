package interfaces.rest;

import interfaces.rabbitmq.ReportFactory;
import interfaces.rabbitmq.TransactionFactory;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import services.ReportService;

import javax.enterprise.event.Observes;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.logging.Logger;

@OpenAPIDefinition(
        info = @Info(
                title = "Group 11 - report-service API",
                version = "3.0.3"
        ))
@ApplicationPath("/api/v1")
public class RootApplication extends Application {

    private final static Logger LOGGER = Logger.getLogger(RootApplication.class.getName());

    private final ReportService reportService = new ReportService();

    public RootApplication() {
        super();
    }

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        new ReportFactory().getService(reportService);
        new TransactionFactory().getService(reportService);
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

}
