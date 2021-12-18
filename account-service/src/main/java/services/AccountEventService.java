/**
 * @author Kasper (s141250)
 * @author Sebastian (s135243)
 * @author most other team members
 */

package services;

import com.google.gson.Gson;
import dto.PaymentAccounts;
import dto.PaymentRequest;
import dto.UserAccountDTO;
import dto.UserRegistrationDTO;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;
import services.interfaces.IAccountService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountEventService implements EventReceiver {

    private final static Logger LOGGER = Logger.getLogger(AccountEventService.class.getName());
    private final EventSender eventSender;

    private final IAccountService service;
    private final Gson gson = new Gson();

    public AccountEventService(EventSender eventSender, AccountService service) {
        this.eventSender = eventSender;
        this.service = service;
    }

    @Override
    public void receiveEvent(Event eventIn) throws Exception {
        switch (eventIn.getEventType()) {
            case "Register":
                try {
                    UserRegistrationDTO userRegistrationDTO = gson.fromJson(gson.toJson(eventIn.getArguments()[0]), UserRegistrationDTO.class);
                    String internalId = service.register(userRegistrationDTO);
                    Event eventOut = new Event("RegisterSuccessful", new Object[]{internalId});
                    eventSender.sendEvent(eventOut);
                } catch (Exception e) {
                    Event eventOut = new Event("RegisterFailed", new Object[]{e.getMessage()});
                    eventSender.sendEvent(eventOut);
                }
                break;
            case "GetAccount":
                try {
                    String id = (String) eventIn.getArguments()[0];
                    UserAccountDTO userAccountDTO = service.get(id);
                    Event eventOut = new Event("GetAccountSuccessful", new Object[]{userAccountDTO});
                    eventSender.sendEvent(eventOut);
                } catch (Exception e) {
                    Event eventOut = new Event("GetAccountFailed", new Object[]{e.getMessage()});
                    eventSender.sendEvent(eventOut);
                }
                break;
            case "GetAccountByCpr":
                try {
                    String cpr = (String) eventIn.getArguments()[0];
                    UserAccountDTO userAccountDTO = service.getByCpr(cpr);
                    Event eventOut = new Event("GetAccountByCprSuccessful", new Object[]{userAccountDTO});
                    eventSender.sendEvent(eventOut);
                } catch (Exception e) {
                    Event eventOut = new Event("GetAccountByCprFailed", new Object[]{e.getMessage()});
                    eventSender.sendEvent(eventOut);
                }
                break;
            case "RetireAccount":
                try {
                    String id = (String) eventIn.getArguments()[0];
                    service.retireAccount(id);
                    Event eventOut = new Event("RetireAccountSuccessful");
                    eventSender.sendEvent(eventOut);
                } catch (Exception e) {
                    Event eventOut = new Event("RetireAccountFailed", new Object[]{e.getMessage()});
                    eventSender.sendEvent(eventOut);
                }
                break;
            case "RetireAccountByCpr":
                try {
                    String cpr = (String) eventIn.getArguments()[0];
                    service.retireAccountByCpr(cpr);
                    Event eventOut = new Event("RetireAccountByCprSuccessful");
                    eventSender.sendEvent(eventOut);
                } catch (Exception e) {
                    Event eventOut = new Event("RetireAccountByCprFailed", new Object[]{e.getMessage()});
                    eventSender.sendEvent(eventOut);
                }
                break;
            case "GetAllAccounts":
                try {
                    List<UserAccountDTO> userAccountDTOs = service.getAll();
                    Event eventOut = new Event("GetAllAccountsSuccessful", new Object[]{userAccountDTOs});
                    eventSender.sendEvent(eventOut);
                } catch (Exception e) {
                    Event eventOut = new Event("GetAllAccountsFailed", new Object[]{e.getMessage()});
                    eventSender.sendEvent(eventOut);
                }
                break;
            case "ProcessPayment":
                try {
                    PaymentRequest paymentRequest = gson.fromJson(gson.toJson(eventIn.getArguments()[0]), PaymentRequest.class);

                    UserAccountDTO customerAccount = service.get(paymentRequest.getCustomerId());
                    UserAccountDTO merchantAccount = service.get(paymentRequest.getMerchantId());

                    PaymentAccounts paymentAccounts = new PaymentAccounts();
                    paymentAccounts.setCustomer(customerAccount);
                    paymentAccounts.setMerchant(merchantAccount);
                    paymentAccounts.setAmount(paymentRequest.getAmount());
                    paymentAccounts.setToken(paymentRequest.getToken());

                    Event eventOut = new Event("PaymentAccountsSuccessful", new Object[]{paymentAccounts});
                    eventSender.sendEvent(eventOut);
                } catch (Exception e) {
                    PaymentRequest paymentRequest = gson.fromJson(gson.toJson(eventIn.getArguments()[0]), PaymentRequest.class);
                    Event eventOut = new Event("PaymentAccountsFailed", new Object[]{paymentRequest});
                    eventSender.sendEvent(eventOut);
                }
                break;
            default:
                LOGGER.log(Level.WARNING, "Ignored event with type: " + eventIn.getEventType() + ". Event: " + eventIn.toString());
                break;
        }
    }
}
