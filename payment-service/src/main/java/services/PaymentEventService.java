package services;

import com.google.gson.Gson;
import dto.PaymentAccounts;
import dto.PaymentRequest;
import dto.TransactionDTO;
import exceptions.SendEventFailedException;
import infrastructure.bank.BankService;
import infrastructure.bank.BankServiceException_Exception;
import infrastructure.bank.BankServiceService;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @primary-author Troels (s161791)
 * @co-author Daniel (s151641)
 */
public class PaymentEventService implements EventReceiver {

    private final static Logger LOGGER = Logger.getLogger(PaymentEventService.class.getName());
    private final EventSender eventSender;
    private final BankService bs;
    private final Gson gson = new Gson();


    public PaymentEventService(EventSender eventSender) {
        this.eventSender = eventSender;
        bs = new BankServiceService().getBankServicePort();
    }

    public PaymentEventService(EventSender eventSender, BankService bank) {
        this.eventSender = eventSender;
        bs = bank;
    }
    @Override
    public void receiveEvent(Event event) {
        switch (event.getEventType()) {
            case "PaymentAccountsFailed":
                PaymentAccountFailed(event);
                break;
            case "TokenValidationSuccessful":
                try {
                    receivedTokenValidationSuccessful(event);
                } catch (SendEventFailedException e) {
                    LOGGER.severe(e.getMessage());
                    e.printStackTrace();
                }
                break;
            case "TokenValidationFailed":
                TokenValidationFailed(event);
                break;
            default:
                LOGGER.info("Ignored event with type: " + event.getEventType() + ". Event: " + event.toString());
                break;
        }
    }

    private void TokenValidationFailed(Event event) {
        PaymentAccounts paymentAccounts = gson.fromJson(gson.toJson(event.getArguments()[0]), PaymentAccounts.class);

        String customerId = paymentAccounts.getCustomer().getId();
        String merchantId = paymentAccounts.getMerchant().getId();
        String customerBankId = paymentAccounts.getCustomer().getBankAccount().getId();
        String merchantBankId = paymentAccounts.getMerchant().getBankAccount().getId();
        String token = paymentAccounts.getToken();
        int amount = paymentAccounts.getAmount();

        String desc = "Transaction between Customer (Id: " + customerId + ") " +
                " (BankId: " + customerBankId + ")" +
                " and Merchant (Id: " + merchantId + ")" +
                " (BankId: " + merchantBankId + " for amount " + amount +
                " with token " + token;

        TransactionDTO dto = new TransactionDTO(BigDecimal.valueOf(amount), paymentAccounts.getCustomer().getBankAccount().getBalance(), merchantId, customerId, desc, new Date(), false);

        try {
            eventSender.sendEvent(new Event("PaymentFailed", new Object[]{dto}));
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    private void PaymentAccountFailed(Event event) {
        PaymentRequest paymentRequest = gson.fromJson(gson.toJson(event.getArguments()[0]), PaymentRequest.class);
        String desc = "Transaction between Customer (Id: " + paymentRequest.getCustomerId()+ ") " +
                " and Merchant (Id: " + paymentRequest.getMerchantId() + ")" +
                " for amount " + paymentRequest.getAmount() +
                " with token " + paymentRequest.getToken();
        TransactionDTO dto = new TransactionDTO(BigDecimal.valueOf(paymentRequest.getAmount()), BigDecimal.valueOf(-1), paymentRequest.getMerchantId(), paymentRequest.getCustomerId(), desc, new Date(), false);
        try {
            eventSender.sendEvent(new Event("PaymentFailed", new Object[]{dto}));
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    private void receivedTokenValidationSuccessful(Event event) throws SendEventFailedException {
        PaymentAccounts paymentAccounts = gson.fromJson(gson.toJson(event.getArguments()[0]), PaymentAccounts.class);

        String customerId = paymentAccounts.getCustomer().getId();
        String merchantId = paymentAccounts.getMerchant().getId();
        String customerBankId = paymentAccounts.getCustomer().getBankAccount().getId();
        String merchantBankId = paymentAccounts.getMerchant().getBankAccount().getId();
        String token = paymentAccounts.getToken();
        int amount = paymentAccounts.getAmount();

        String desc = "Transaction between Customer (Id: " + customerId + ") " +
                " (BankId: " + customerBankId + ")" +
                " and Merchant (Id: " + merchantId + ")" +
                " (BankId: " + merchantBankId + " for amount " + amount +
                " with token " + token;

        try {
            bs.transferMoneyFromTo(customerBankId, merchantBankId, BigDecimal.valueOf(paymentAccounts.getAmount()), desc);

            BigDecimal newCustomerBalance = BigDecimal.valueOf(paymentAccounts.getCustomer().getBankAccount().getBalance().intValue() - amount);
            TransactionDTO dto = new TransactionDTO(BigDecimal.valueOf(amount), newCustomerBalance, merchantId, customerId, desc, new Date(), true);

            Event eventOut = new Event("PaymentSuccessful", new Object[]{dto});
            eventSender.sendEvent(eventOut);
        } catch (BankServiceException_Exception e) {
            TransactionDTO dto = new TransactionDTO(BigDecimal.valueOf(amount), paymentAccounts.getCustomer().getBankAccount().getBalance(), merchantId, customerId, desc, new Date(), false);
            Event eventOut = new Event("PaymentFailed", new Object[]{dto});
            try {
                eventSender.sendEvent(eventOut);
            } catch (Exception exception) {
                throw new SendEventFailedException(e.getMessage());
            }
        } catch (Exception e) {
            throw new SendEventFailedException(e.getMessage());
        }
    }

    public void SendTestEvent(String type, Object[] args) throws Exception {
        Event event = new Event(type, args);
        eventSender.sendEvent(event);
    }

}