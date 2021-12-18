/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 */

package cucumber.steps;

import dto.MoneySummary;
import dto.TransactionDTO;
import exceptions.transaction.TransactionException;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.restassured.internal.common.assertion.Assertion;
import org.junit.After;
import services.ReportReceiverService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.junit.jupiter.api.Assertions;
import services.ReportService;
import services.TransactionSpyService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AdminSteps {
    ReportReceiverService rr;
    TransactionSpyService tss;
    ReportService rs = new ReportService();
    Event event;
    List<TransactionDTO> inputTransactions = new ArrayList<>();
    private final CompletableFuture<List<TransactionDTO>> result = new CompletableFuture<>();
    private final CompletableFuture<MoneySummary> summary = new CompletableFuture<>();
    TransactionDTO transaction;
    private Exception e;

    public AdminSteps() {
        rr = new ReportReceiverService(ev -> event = ev, rs);
        tss = new TransactionSpyService(ev -> event = ev, rs);
    }

    @Given("a list of transactions")
    public void aListOfTransactions(DataTable table) throws Exception {
        rs.getRepo().dropEverything();
        inputTransactions = new ArrayList<>();
        for (Map<Object, Object> row : table.asMaps(String.class, String.class)) {
            TransactionDTO transaction = new TransactionDTO();
            transaction.setAmount(new BigDecimal((String) row.get("amount")));
            transaction.setBalance(new BigDecimal((String) row.get("balance")));
            transaction.setCreditor((String) row.get("creditor"));
            transaction.setDebtor((String) row.get("debtor"));
            transaction.setDescription((String) row.get("description"));
            transaction.setTime(new Date());
            transaction.setToken((String) row.get("token"));
            inputTransactions.add(transaction);
            rs.getRepo().add(transaction);
        }
    }

    @When("a request to see all transactions is made")
    public void aRequestToSeeAllTransactionsIsMade() {
        new Thread(() -> {try {
            result.complete(rr.requestAllTransactions());
        } catch (Exception e) {
            throw new Error(e);
        }}).start();
    }

    @Then("the list of transactions is shown")
    public void theListOfTransactionsIsShown() {
        Assertions.assertEquals(inputTransactions, result.join());
    }

    @When("a request for summary is made")
    public void aRequestForSummaryIsMade() {
        new Thread(() -> {try {
            summary.complete(rr.requestSummary());
        } catch (Exception e) {
            throw new Error(e);
        }}).start();
    }

    @Then("a summary is made based on the transactions")
    public void aSummaryIsMadeBasedOnTheTransactions(DataTable table) {
        Assertions.assertEquals(table.asMaps(String.class, BigDecimal.class).get(0), summary.join().getSummary());
    }

    @Then("an event {string} has been sent")
    public void anEventHasBeenSent(String eventType) {
        Assertions.assertEquals(eventType,event.getEventType());
    }

    @When("an event {string} has been sent back")
    public void anEventHasBeenSentBack(String eventType) {
        rr.receiveEvent(new Event(eventType, new Object[] {inputTransactions}));
    }

    @When("a new broken transaction is recorded")
    public void aNewBrokenTransactionIsRecorded() {
        rs.getRepo().dropEverything();
        transaction = new TransactionDTO(new BigDecimal(100), new BigDecimal(1000), "1234", "2345", "thistest", new Date(), false);
        transaction.setToken("1234");
        transaction.setAmount(null);
        try {
            tss.receiveEvent(new Event("PaymentSuccessful", new Object[] {transaction}));
        } catch (Exception e) {
            this.e = e;
        }
    }

    @Then("an exception is returned: {string}")
    public void anExceptionIsReturned(String error) {
        Assertions.assertEquals(error, e.getMessage());
    }

    @When("the transaction is recorded")
    public void theTransactionIsRecorded() throws Exception {
        rs.getRepo().dropEverything();
        transaction = new TransactionDTO(new BigDecimal(100), new BigDecimal(1000), "1234", "2345", "thistest", new Date(), false);
        transaction.setToken("1234");
        tss.receiveEvent(new Event("PaymentSuccessful", new Object[] {transaction}));
    }

    @Then("a {string} is sent")
    public void aIsSent(String eventType) {
        Assertions.assertEquals(eventType, event.getEventType());
    }

    @And("the transaction can be found in the repository")
    public void theTransactionCanBeFoundInTheRepository() {
        Assertions.assertEquals(transaction.toString(), rs.getRepo().getAll().get(0).toString());
    }
}
