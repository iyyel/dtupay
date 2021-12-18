/**
 *  @primary-author Emil (s174265)
 *  @co-author Tobias (s173899)
 */
package cucumber.steps;

import dto.CustomerTokens;
import exceptions.CustomerNotFoundException;
import interfaces.rabbitmq.TokenListener;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.junit.jupiter.api.Assertions;
import services.TokenEventService;
import services.TokenService;

public class requestTokensSteps {
    String customerId;
    TokenService ts = new TokenService();
    //private final CompletableFuture<List<TransactionDTO>> result = new CompletableFuture<>();
    Exception e;
    Event event;
    TokenListener tl;
    TokenEventService tes;

    public requestTokensSteps(){
        tes = new TokenEventService(ev -> event = ev, ts);

    }

    @Given("^the customer with id \"([^\"]*)\"$")
    public void theCustomerWithId(String cid) {
        this.customerId = cid;
        ts.registerCustomer(cid);
    }

    @And("^the customer has (\\d+) tokens$")
    public void theCustomerHasTokens(int amount) {
        try {
            tes.receiveEvent(new Event("RequestTokens", new Object[]{customerId, amount}));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Then("^the customer owns (\\d+) tokens$")
    public void theCustomerOwnsTokens(int amount) throws CustomerNotFoundException {
        CustomerTokens customer = ts.getCustomer(customerId);
        Assertions.assertEquals(amount, customer.getTokens().size());
    }

    @Then("^an exception is returned with the message \"([^\"]*)\"$")
    public void anExceptionIsReturnedWithTheMessage(String errormessage) {
        Assertions.assertEquals(errormessage, e.getMessage());
        e = null;
    }

    @Given("no customer exists with id {string}")
    public void noCustomerExistsWithId(String cid) {
        customerId = cid;
        Assertions.assertFalse(ts.customerExists(cid));
    }

    @When("the customer is deleted")
    public void theCustomerIsDeleted() {
        try {
            tes.receiveEvent(new Event("RetireCustomerTokens", new Object[]{customerId}));
        } catch (Exception e) {
            this.e = e;
        }
    }

    @Then("the customer is not found")
    public void theCustomerIsNotFound() {
        Assertions.assertEquals("CustomerRetirementSuccessful", event.getEventType());
        Assertions.assertThrows(CustomerNotFoundException.class, () -> ts.getCustomer(customerId));
    }

    @When("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int amount) {
        try {
            tes.receiveEvent(new Event("RequestTokens", new Object[]{customerId, amount}));
        } catch (Exception e) {
            this.e = e;
        }
    }

    @Then("an event of type {string} is returned")
    public void anEventOfTypeIsReturned(String eventType) {
        Assertions.assertEquals(eventType, event.getEventType());
    }

    @Then("an error for customer {string} is received")
    public void anErrorForCustomerIsReceived(String failedCustomer) {
        Assertions.assertEquals(failedCustomer, event.getArguments()[0]);
    }
}

