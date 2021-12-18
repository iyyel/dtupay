/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 *
 * Event receiver which logs every transactions to a repo.
 */

package services;

import com.google.gson.Gson;
import dto.PaymentRequest;
import dto.TransactionDTO;
import exceptions.transaction.TransactionException;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;

public class TransactionSpyService implements EventReceiver {

    private final EventSender eventSender;
    private final ReportService rs;
    private Gson gson = new Gson();

    public TransactionSpyService(EventSender eventSender, ReportService reportService) {
        this.eventSender = eventSender;
        this.rs = reportService;
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        System.out.println("handling event: " + event);
        if (event.getEventType().equals("PaymentSuccessful") || event.getEventType().equals("PaymentFailed")) {
            handleEvent(event);
        }
    }

    private void handleEvent(Event event) throws Exception {
        TransactionDTO transaction = gson.fromJson(gson.toJson(event.getArguments()[0]), TransactionDTO.class);
        Event newEvent = new Event("TransactionRecordingSuccessful", new Object[]{transaction});
        try {
            rs.addToRepo(transaction);
        } catch (TransactionException e) {
            System.out.println("Transaction recording failed!");
            newEvent = new Event("TransactionRecordingFailed", new Object[]{transaction});
        }

        eventSender.sendEvent(newEvent);
    }
}
