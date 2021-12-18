package interfaces.rabbitmq;

import messaging.EventSender;
import services.AccountEventService;
import services.AccountService;

/**
 * @primary-author Daniel (s151641)
 * @co-author Troels (s161791)
 * <p>
 * Thanks to Hubert Baumeister (huba@dtu.dk) for initial
 * rabbitMQ implementation template.
 */
public class AccountFactory {

    static AccountEventService accountEventService = null;

    public AccountEventService getService() {
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
        accountEventService = new AccountEventService(b, new AccountService());
        AccountListener r = new AccountListener(accountEventService);
        try {
            r.listen();
        } catch (Exception e) {
            throw new Error(e);
        }
        return accountEventService;
    }
}
