package services;

import dto.Token;
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
public class TokenEventService implements EventReceiver {

    private final static Logger LOGGER = Logger.getLogger(TokenEventService.class.getName());
    private final EventSender eventSender;
    private CompletableFuture<Boolean> requestTokensResult;
    private CompletableFuture<Token> getTokenResult;
    private CompletableFuture<Boolean> retireCustomerTokensResult;

    public TokenEventService(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    @Override
    public void receiveEvent(Event event) {
        LOGGER.info("Received event: " + event);
        switch (event.getEventType()) {
            case "RequestTokensSuccessful":
                requestTokensResult.complete(true);
                break;
            case "RequestTokensFailed":
                requestTokensResult.complete(false);
                break;
            case "GetTokenSuccessful":
                String tokenId = (String) event.getArguments()[0];
                //String id = (String) event.getArguments()[1];
                Token token = new Token(tokenId);
                getTokenResult.complete(token);
                break;
            case "GetTokenFailed":
                getTokenResult.complete(null);
                break;
            case "CustomerRetirementSuccessful":
                retireCustomerTokensResult.complete(true);
                break;
            case "CustomerRetirementFailed":
                retireCustomerTokensResult.complete(false);
                break;
            default:
                LOGGER.log(Level.WARNING, "Ignored event with type: " + event.getEventType() + ". Event: " + event.toString());
                break;
        }
    }

    public void sendRequestTokensEvent(String id, int amount)
            throws SendEventFailedException, EventFailedException {
        String eventType = "RequestTokens";
        Object[] arguments = new Object[]{id, amount};
        Event event = new Event(eventType, arguments);
        requestTokensResult = new CompletableFuture<>();

        try {
            eventSender.sendEvent(event);
            LOGGER.info("Sent event: " + event);

            boolean isSuccessful = requestTokensResult.join();

            if (!isSuccessful) {
                throw new EventFailedException(eventType + " event has failed for account id: " + id + " and token amount: " + amount);
            }
        } catch (EventFailedException e) {
            throw new EventFailedException(e.getMessage());
        } catch (Exception e) {
            throw new SendEventFailedException("requestTokens event failed to send!");
        }
    }

    public Token sendGetTokenEvent(String id)
            throws SendEventFailedException, EventFailedException {
        String eventType = "GetToken";
        Object[] arguments = new Object[]{id};
        Event event = new Event(eventType, arguments);
        getTokenResult = new CompletableFuture<>();

        try {
            eventSender.sendEvent(event);
            LOGGER.info("Sent event: " + event);

            Token token = getTokenResult.join();

            if (token == null) {
                throw new EventFailedException(eventType + " event has failed for account id: " + id);
            }
            return token;
        } catch (EventFailedException e) {
            throw new EventFailedException(e.getMessage());
        } catch (Exception e) {
            throw new SendEventFailedException("getToken event failed to send!");
        }
    }

    public void sendRetireCustomerTokensEvent(String id)
            throws SendEventFailedException, EventFailedException {
        String eventType = "RetireCustomerTokens";
        Object[] arguments = new Object[]{id};
        Event event = new Event(eventType, arguments);
        retireCustomerTokensResult = new CompletableFuture<>();

        try {
            eventSender.sendEvent(event);
            LOGGER.info("Sent event: " + event);

            boolean isSuccessful = retireCustomerTokensResult.join();

            if (!isSuccessful) {
                throw new EventFailedException(eventType + " event has failed for account id: " + id);
            }
        } catch (EventFailedException e) {
            throw new EventFailedException(e.getMessage());
        } catch (Exception e) {
            throw new SendEventFailedException("RetireCustomerTokens event failed to send!");
        }
    }

}