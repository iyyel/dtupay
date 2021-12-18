package main;

import interfaces.rabbitmq.payment.PaymentEventServiceFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @primary-author Troels (s161791)
 * @co-author Daniel (s151641)
 */
public class Main {

    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.info("The application is starting...");
        try {
            // starting the RabbitMQ listeners
            new PaymentEventServiceFactory().getService();
            LOGGER.log(Level.INFO, "RabbitMQ listening...");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "RabbitMQ failed to start: " + e.getMessage());
        }
    }

}