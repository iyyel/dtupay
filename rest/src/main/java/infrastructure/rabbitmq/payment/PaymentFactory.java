package infrastructure.rabbitmq.payment;

import messaging.EventSender;
import services.PaymentEventService;

/**
 * @primary-author Daniel (s151641)
 * @co-author Troels (s161791)
 * <p>
 * Thanks to Hubert Baumeister (huba@dtu.dk) for initial
 * rabbitMQ implementation template.
 */
public class PaymentFactory {

    static PaymentEventService paymentEventService = null;

    public static PaymentEventService getService() {
        // The singleton pattern.
        // Ensure that there is at most
        // one instance of a PaymentService
        if (paymentEventService != null) {
            return paymentEventService;
        }

        // Hookup the classes to send and receive
        // messages via RabbitMq, i.e. RabbitMqSender and
        // RabbitMqListener.
        // This should be done in the factory to avoid
        // the PaymentService knowing about them. This
        // is called dependency injection.
        // At the end, we can use the PaymentService in tests
        // without sending actual messages to RabbitMq.
        EventSender b = new PaymentSender();
        paymentEventService = new PaymentEventService(b);
        PaymentListener r = new PaymentListener(paymentEventService);
        try {
            r.listen();
        } catch (Exception e) {
            throw new Error(e);
        }
        return paymentEventService;
    }
}
