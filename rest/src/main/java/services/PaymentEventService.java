package services;

import com.google.gson.Gson;
import dto.PaymentRequest;
import dto.TransactionDTO;
import exceptions.EventFailedException;
import exceptions.SendEventFailedException;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @primary-author Troels (s161791)
 * @co-author Daniel (s151641)
 */
public class PaymentEventService implements EventReceiver {

    private final static Logger LOGGER = Logger.getLogger(PaymentEventService.class.getName());
    private final EventSender eventSender;
    private final Gson gson = new Gson();
    private CompletableFuture<TransactionDTO> processPaymentResult;
    private CompletableFuture<TransactionDTO> processRefundResult;

    public PaymentEventService(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    @Override
    public void receiveEvent(Event event) {
        LOGGER.info("Received event: " + event);
        switch (event.getEventType()) {
            case "PaymentSuccessful":
                TransactionDTO transactionDTO = gson.fromJson(gson.toJson(event.getArguments()[0]), TransactionDTO.class);
                transactionDTO.setSuccessful(true);
                processPaymentResult.complete(transactionDTO);
                break;
            case "PaymentFailed":
                transactionDTO = gson.fromJson(gson.toJson(event.getArguments()[0]), TransactionDTO.class);
                transactionDTO.setSuccessful(false);
                processPaymentResult.complete(transactionDTO);
                break;
/*            case "RefundSuccessful":
                transactionDTO = gson.fromJson(gson.toJson(event.getArguments()[0]), TransactionDTO.class);
                transactionDTO.setSuccessful(true);
                processRefundResult.complete(transactionDTO);
                break;
            case "RefundFailed":
                transactionDTO = gson.fromJson(gson.toJson(event.getArguments()[0]), TransactionDTO.class);
                transactionDTO.setSuccessful(false);
                processRefundResult.complete(transactionDTO);
                break;*/
            default:
                LOGGER.log(Level.WARNING, "Ignored event with type: " + event.getEventType() + ". Event: " + event.toString());
                break;
        }
    }

    public TransactionDTO sendProcessPaymentEvent(PaymentRequest paymentRequest)
            throws SendEventFailedException, EventFailedException {
        String eventType = "ProcessPayment";
        Object[] arguments = new Object[]{paymentRequest};
        Event event = new Event(eventType, arguments);
        processPaymentResult = new CompletableFuture<>();

        try {
            eventSender.sendEvent(event);
            LOGGER.info("Sent event: " + event);

            TransactionDTO transactionDTO = processPaymentResult.join();

            if (transactionDTO == null) {
                throw new EventFailedException(eventType + " event has failed for paymentRequest: " + paymentRequest);
            }

            return transactionDTO;
        } catch (EventFailedException e) {
            throw new EventFailedException(e.getMessage());
        } catch (Exception e) {
            throw new SendEventFailedException("ProcessPayment event failed to send!");
        }
    }

    public TransactionDTO sendProcessRefundEvent(PaymentRequest paymentRequest)
            throws EventFailedException, SendEventFailedException {
        String customerId = paymentRequest.getCustomerId();
        paymentRequest.setCustomerId(paymentRequest.getMerchantId());
        paymentRequest.setMerchantId(customerId);
        return sendProcessPaymentEvent(paymentRequest);
    }

}