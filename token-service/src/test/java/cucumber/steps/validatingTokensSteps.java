/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package cucumber.steps;

import dto.PaymentAccounts;
import dto.Token;
import exceptions.TokenNotFoundException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.junit.jupiter.api.Assertions;
import services.TokenEventService;
import services.TokenService;

public class validatingTokensSteps {
    String customerId, foundCustomerId;
    TokenService ts = new TokenService();
    Token token;
    Exception e;
    Event event;
    TokenEventService tes;
    PaymentAccounts paymentAccounts;

    public validatingTokensSteps(){
        tes = new TokenEventService(ev -> event = ev, ts);
    }

    @Given("a customer with id {string}")
    public void aCustomerWithId(String cid) {
        customerId = cid;
        ts.registerCustomer(cid);
    }

    @And("has an unused token")
    public void hasAnUnusedToken() {
        try {
            tes.receiveEvent(new Event("RequestTokens", new Object[]{customerId, 1}));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @And("^a token is sent to the server")
    public void aTokenIsSentToTheServer() {
        try {
            foundCustomerId = ts.getCustomerFromToken(token.getId()).getCustomerId();
            PaymentAccounts paymentAccounts = new PaymentAccounts();
            paymentAccounts.setToken(token.getId());
            tes.receiveEvent(new Event("PaymentAccountsSuccessful", new Object[]{paymentAccounts}));
        } catch (Exception e) {
            this.e = e;
        }
    }

    @Then("^the token is invalidated$")
    public void theTokenIsInvalidated() {
        System.out.println(event.getEventType());
        Assertions.assertThrows(TokenNotFoundException.class, () -> ts.validateToken(token.getId()));
        Assertions.assertNull(e);
    }

    @And("^the customer \"([^\"]*)\" is returned$")
    public void theCustomerIsReturned(String cid) {
        Assertions.assertEquals(cid, foundCustomerId);
    }

    @Then("a TokenNotFound exception is returned")
    public void aTokenNotFoundExceptionIsReturned() {
        Assertions.assertEquals("Token " + token.getId() + " can not be found.", e.getMessage());
    }

    @And("the token is deleted")
    public void theTokenIsDeleted() {
        try {
            ts.validateToken(token.getId());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @And("the customer receives a token")
    public void theCustomerReceivesAToken() {
        try {
            tes.receiveEvent(new Event("GetToken", new Object[]{customerId}));
            token = new Token((String) event.getArguments()[0]);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @When("the customer tries to receive a token")
    public void theCustomerTriesToReceiveAToken() {
        try {
            tes.receiveEvent(new Event("GetToken", new Object[]{customerId}));
            token = new Token((String) event.getArguments()[0]);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Then("an event of type {string} is received")
    public void anEventOfTypeIsReceived(String eventType) {
        Assertions.assertEquals(eventType, event.getEventType());
    }

    @Given("a broken validation paymentAccounts")
    public void aBrokenValidationPaymentAccounts() {
        paymentAccounts = new PaymentAccounts();
        paymentAccounts.setToken(null);
    }

    @And("it gets a token successfully")
    public void itGetsATokenSuccessfully() {
        Assertions.assertEquals("GetTokenSuccessful", event.getEventType());
    }

    @When("the null token is sent to the server")
    public void theNullTokenIsSentToTheServer() {
        try {
            tes.receiveEvent(new Event("PaymentAccountsSuccessful", new Object[]{paymentAccounts}));
        } catch (Exception e) {
            this.e = e;
        }
    }

    @And("an error for customer {string} is returned")
    public void anErrorForCustomerIsReturned(String failedCustomer) {
        Assertions.assertEquals(failedCustomer, event.getArguments()[0]);
    }
}