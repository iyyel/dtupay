package infrastructure.rabbitmq.account;

import messaging.EventSender;
import services.AccountEventService;

/**
 * @primary-author Kasper (s141250)
 * @co-author Sebastian (s135243)
 * <p>
 * Thanks to Hubert Baumeister (huba@dtu.dk) for initial
 * rabbitMQ implementation template.
 */
public class AccountFactory {

    static AccountEventService accountEventService = null;

    public static AccountEventService getService() {
        // The singleton pattern.
        // Ensure that there is at most
        // one instance of a PaymentService
        if (accountEventService != null) {
            return accountEventService;
        }

        // Hookup the classes to send and receive
        // messages via RabbitMq, i.e. RabbitMqSender and
        // RabbitMqListener.
        // This should be done in the factory to avoid
        // the PaymentService knowing about them. This
        // is called dependency injection.
        // At the end, we can use the PaymentService in tests
        // without sending actual messages to RabbitMq.
        EventSender b = new AccountSender();
        accountEventService = new AccountEventService(b);
        AccountListener r = new AccountListener(accountEventService);
        try {
            r.listen();
        } catch (Exception e) {
            throw new Error(e);
        }
        return accountEventService;
    }
}
