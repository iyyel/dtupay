package cucumber;

import dto.BankAccountDTO;
import dto.PaymentAccounts;
import dto.PaymentRequest;
import dto.UserAccountDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import mock.MockBank;
import services.PaymentEventService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentEventsSteps {

    PaymentEventService s;
    Event event;
    PaymentAccounts paymentAccounts;
    MockBank bs;

    public PaymentEventsSteps() {
        bs = new MockBank();
        s = new PaymentEventService(ev -> event = ev, bs);

        UserAccountDTO customerAccount = new UserAccountDTO();
        UserAccountDTO merchantAccount = new UserAccountDTO();
        BankAccountDTO account = new BankAccountDTO();
        account.setBalance(new BigDecimal(1100));
        customerAccount.setBankAccount(account);
        merchantAccount.setBankAccount(account);

        paymentAccounts = new PaymentAccounts();
        paymentAccounts.setCustomer(customerAccount);
        paymentAccounts.setMerchant(merchantAccount);
        paymentAccounts.setAmount(12);
        paymentAccounts.setToken("123");
    }

    @When("a colleague working with accounts is done he notifies me with {string}")
    public void sendAccountEvent(String eventType) {
        try {
            s.SendTestEvent(eventType, new Object[]{paymentAccounts});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("i receive a message {string} it means that one or both accounts does not exist in the bank")
    public void receive_accounts_fail(String eventType) {
        s.receiveEvent(new Event(eventType, new Object[]{new PaymentRequest()}));
    }

    @Then("i notify the parties that the transaction failed with {string}")
    public void sendsEvent(String eventType) {
        assertTrue(event.getEventType().equals(eventType));
    }

    @And("the message should be {string}")
    public void typeCheck(String eventType) {
        assertTrue(event.getEventType().equals(eventType));
    }


    @When("a colleague working with tokens is done he notifies me with {string}")
    public void endTokenEvent(String eventType) {
        s.receiveEvent(new Event(eventType, new Object[]{paymentAccounts}));
    }

    @Then("i receive {string}")
    public void receive(String eventType) {
        s.receiveEvent(new Event(eventType, new Object[]{paymentAccounts}));
    }

    @Then("i receive {string} but something goes wrong")
    public void receivefail(String eventType) {
        paymentAccounts.setAmount(-1); // force it to fail
        s.receiveEvent(new Event(eventType, new Object[]{paymentAccounts}));
        paymentAccounts.setAmount(12); // reset
    }


    @Then("i have to notify the parties that the transaction failed with {string}")
    public void notify(String eventType) {
        assertTrue(event.getEventType().equals(eventType));
    }

    @Then("i check if the bank have transferred money")
    public void moveMoneyCheck() {
        assertTrue(bs.haveBeenNotified);
    }

    @Then("i have to notify the parties that the money as been moved by sending {string}")
    public void successpayment(String eventType) {
        assertTrue(event.getEventType().equals(eventType));
    }

    @When("the bank denies the transaction")
    public void denies() {
        assertTrue(event.getEventType().equals("PaymentFailed"));
    }
}
