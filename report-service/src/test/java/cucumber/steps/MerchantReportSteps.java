/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 */

package cucumber.steps;

import dto.TransactionDTO;
import exceptions.transaction.TransactionException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import org.junit.jupiter.api.Assertions;
import services.ReportReceiverService;
import services.ReportService;
import services.TransactionSpyService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MerchantReportSteps {
    ReportReceiverService rr;
    TransactionSpyService tss;
    ReportService rs = new ReportService();
    List<TransactionDTO> inputTransactions = new ArrayList<>();
    private final CompletableFuture<List<TransactionDTO>> result = new CompletableFuture<>();
    Event event;

    public MerchantReportSteps() {
        rr = new ReportReceiverService(ev -> event = ev, rs);
        tss = new TransactionSpyService(ev -> event = ev, rs);
    }

    @Given("a list of merchant transactions")
    public void aListOfMerchantTransactions(DataTable table) throws TransactionException, DatatypeConfigurationException {
        rs.getRepo().dropEverything();
        inputTransactions = new ArrayList<>();
        for (Map<Object, Object> row : table.asMaps(String.class, String.class)) {
            TransactionDTO transaction = new TransactionDTO();
            transaction.setAmount(new BigDecimal((String) row.get("amount")));
            transaction.setBalance(new BigDecimal((String) row.get("balance")));
            transaction.setCreditor((String) row.get("creditor"));
            transaction.setDebtor((String) row.get("debtor"));
            transaction.setDescription((String) row.get("description"));
            if (row.get("time") != null) {
                XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar((String) row.get("time"));
                transaction.setTime(calendar.toGregorianCalendar().getTime());
            }
            transaction.setToken((String) row.get("token"));
            inputTransactions.add(transaction);
            rs.getRepo().add(transaction);
        }
    }

    @When("the merchant {string} requests all their transactions")
    public void theMerchantRequestsAllTheirTransactions(String merchantId) {
        new Thread(() -> {try {
            result.complete(rr.requestAllMerchantTransactions(merchantId));
        } catch (Exception e) {
            throw new Error(e);
        }}).start();
    }

    @When("the merchant {string} requests their transactions between {string} and {string}")
    public void theMerchantRequestsTheirTransactionsBetweenAnd(String merchantId, String beg, String end) throws DatatypeConfigurationException {
        XMLGregorianCalendar begTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(beg);
        XMLGregorianCalendar endTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(end);
        new Thread(() -> {try {
            result.complete(rr.requestAllMerchantTransactionsBetween(merchantId, begTime.toGregorianCalendar().getTime(), endTime.toGregorianCalendar().getTime()));
        } catch (Exception e) {
            throw new Error(e);
        }}).start();
    }

    @Then("the merchant transactions are displayed")
    public void theMerchantTransactionsAreDisplayedBetweenAnd(DataTable table) throws DatatypeConfigurationException {
        ArrayList<TransactionDTO> outputTransactions = new ArrayList<>();
        for (Map<Object, Object> row : table.asMaps(String.class, String.class)) {
            TransactionDTO transaction = new TransactionDTO();
            transaction.setAmount(new BigDecimal((String) row.get("amount")));
            transaction.setCreditor((String) row.get("creditor"));
            transaction.setDescription((String) row.get("description"));
            XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar((String) row.get("time"));
            transaction.setTime(calendar.toGregorianCalendar().getTime());
            transaction.setToken((String) row.get("token"));
            outputTransactions.add(transaction);
        }
        Assertions.assertEquals(outputTransactions.toString(), result.join().toString());
    }
}
