/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 * @template Hubert Baumeister
 *
 * Factory service for REST calls based on code given by Hubert
 */

package interfaces.rabbitmq;

import messaging.EventSender;
import services.ReportReceiverService;
import services.ReportService;

public class ReportFactory {
    static ReportReceiverService reportReceiverService = null;

    public ReportReceiverService getService(ReportService reportService) {
        // The singleton pattern.
        // Ensure that there is at most
        // one instance of a PaymentService
        if (reportReceiverService != null) {
            return reportReceiverService;
        }

        // Hookup the classes to send and receive
        // messages via RabbitMq, i.e. RabbitMqSender and
        // RabbitMqListener.
        // This should be done in the factory to avoid
        // the PaymentService knowing about them. This
        // is called dependency injection.
        // At the end, we can use the PaymentService in tests
        // without sending actual messages to RabbitMq.
        EventSender b = new ReportSender();
        reportReceiverService = new ReportReceiverService(b, reportService);
        ReportListener r = new ReportListener(reportReceiverService);
        try {
            r.listen();
        } catch (Exception e) {
            throw new Error(e);
        }
        return reportReceiverService;
    }
}
