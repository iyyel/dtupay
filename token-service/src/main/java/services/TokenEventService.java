/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 *
 *  RabbitMQ event receiver
 */
package services;

import com.google.gson.Gson;
import dto.PaymentAccounts;
import dto.Token;
import exceptions.CustomerAlreadyRegisteredException;
import exceptions.CustomerNotFoundException;
import exceptions.TooManyTokensException;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;
import services.interfaces.ITokenService;

public class TokenEventService implements EventReceiver {

    ITokenService rs;
    EventSender eventSender;
    private final Gson gson = new Gson();

    public TokenEventService(EventSender eventSender, TokenService tokenService) {
        this.eventSender = eventSender;
        this.rs = tokenService;
    }

    @Override
    public void receiveEvent(Event event) throws Exception {
        System.out.println("handling event: " + event);
        if (event.getEventType().equals("RequestTokens")) {
            try {
                String id = (String) event.getArguments()[0];
                int amount = Double.valueOf(String.valueOf(event.getArguments()[1])).intValue();
                String customerId = rs.requestTokens(id, amount);
                eventSender.sendEvent(new Event("RequestTokensSuccessful", new Object[]{customerId}));
            } catch (CustomerNotFoundException | TooManyTokensException | CustomerAlreadyRegisteredException e) {
                eventSender.sendEvent(new Event("RequestTokensFailed", new Object[]{e.getMessage().split(" ")[1]})); // TODO: Extract this
            }
        }
        if (event.getEventType().equals("GetToken")) {
            try {
                Token token = rs.getToken((String) event.getArguments()[0]);
                eventSender.sendEvent(new Event("GetTokenSuccessful", new Object[]{token.getId(), event.getArguments()[0]}));
            } catch (Exception e) {
                eventSender.sendEvent(new Event("GetTokenFailed", new Object[]{e.getMessage().split(" ")[1]}));
            }
        }

        /*if (event.getEventType().equals("ValidateToken")) {
            try {
                Token token = rs.validateToken((String) event.getArguments()[0]);
                eventSender.sendEvent(new Event("TokenValidationSuccessful", new Object[]{token.getId()}));
            } catch (Exception e) {
                //logger.severe("validateToken threw an exception: " + e.getMessage());
                e.printStackTrace();
                eventSender.sendEvent(new Event("TokenValidationFailed", new Object[]{e.getMessage().split(" ")[1]}));
            }
        }*/

        if (event.getEventType().equals("RetireCustomerTokens")) {
            try {
                String customerId = rs.deleteCustomer((String) event.getArguments()[0]);
                eventSender.sendEvent(new Event("CustomerRetirementSuccessful", new Object[]{customerId}));
            } catch (Exception e) {
                eventSender.sendEvent(new Event("CustomerRetirementFailed", new Object[]{e.getMessage().split(" ")[1]}));
            }
        }
        if (event.getEventType().equals("PaymentAccountsSuccessful")) {
            PaymentAccounts paymentAccounts = gson.fromJson(gson.toJson(event.getArguments()[0]), PaymentAccounts.class);
            try {
                Token token = rs.validateToken(paymentAccounts.getToken());

                Event eventOut = new Event("TokenValidationSuccessful", new Object[]{paymentAccounts});
                eventSender.sendEvent(eventOut);
            } catch (Exception e) {
                eventSender.sendEvent(new Event("TokenValidationFailed", new Object[]{paymentAccounts}));
            }
        }
    }

}