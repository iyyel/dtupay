package cucumber;

import com.app.CustomerApp;
import com.app.MerchantApp;
import com.client.DTUPayClient;
import com.client.ReportClient;
import dto.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * primary-author: Daniel (s151641)
 * co-author: Troels (s161791)
 */
public class PaymentReportSteps {

    private final CustomerApp customerApp = new CustomerApp();
    private final MerchantApp merchantApp = new MerchantApp();

    private final DTUPayClient dtuPay = new DTUPayClient();
    private final ReportClient reportClient = new ReportClient();
    boolean successPayment;

    private UserRegistrationDTO customerRegDTO, merchantRegDTO;

    private UserAccountDTO customerDTO, merchantDTO;

    private String createdCustomerId, createdMerchantId;
    private String currentCustomerId, currentMerchantId;
    private Token customerToken;
    private Integer paymentAmount;

    private TransactionDTO latestTransaction;

    @After
    public void cleanup() {
        dtuPay.retireAccount(createdCustomerId);
        dtuPay.retireCustomerTokens(createdCustomerId);
        dtuPay.retireAccount(createdMerchantId);
    }

    @Given("a new customer with cpr {string}, first name {string}, last name {string} and a balance of {int}")
    public void a_new_customer_with_cpr_first_name(String cpr, String firstName, String lastName, Integer balance) {
        customerRegDTO = new UserRegistrationDTO();
        customerRegDTO.setBankAccount(new BankRegistrationDTO());

        customerRegDTO.setCprNumber(cpr);
        customerRegDTO.setFirstName(firstName);
        customerRegDTO.setLastName(lastName);
        customerRegDTO.getBankAccount().setBalance(BigDecimal.valueOf(balance));
    }

    @When("the customer is registered")
    public void when_the_customer_is_registered() {
        currentCustomerId = dtuPay.registerAccount(customerRegDTO);
        createdCustomerId = currentCustomerId;
    }

    @Then("the customer registration should be successful")
    public void then_the_customer_registration_should_be_successful() {
        assertNotNull(currentCustomerId);
    }

    @And("the new customer should exist in the system")
    public void the_new_customer_exists_in_the_system() {
        customerDTO = dtuPay.getAccount(currentCustomerId);
        Assert.assertNotNull(customerDTO);
    }

    @Then("the customer requests {int} tokens")
    public void the_customer_requests_tokens(Integer tokenAmount) {
        boolean isSuccess = customerApp.requestTokens(currentCustomerId, tokenAmount);
        assertTrue(isSuccess);
    }

    @Given("a new merchant with cpr {string}, first name {string}, last name {string} and a balance of {int}")
    public void a_new_merchant_with_cpr_first_name(String cpr, String firstName, String lastName, Integer balance) {
        merchantRegDTO = new UserRegistrationDTO();
        merchantRegDTO.setBankAccount(new BankRegistrationDTO());

        merchantRegDTO.setCprNumber(cpr);
        merchantRegDTO.setFirstName(firstName);
        merchantRegDTO.setLastName(lastName);
        merchantRegDTO.getBankAccount().setBalance(BigDecimal.valueOf(balance));
    }

    @When("the merchant is registered")
    public void when_the_merchant_is_registered() {
        currentMerchantId = dtuPay.registerAccount(merchantRegDTO);
        createdMerchantId = currentMerchantId;
    }

    @Then("the merchant registration should be successful")
    public void then_the_merchant_registration_should_be_successful() {
        assertNotNull(currentMerchantId);
    }

    @Then("the new merchant exists in the system")
    public void the_new_merchant_exists_in_the_system() {
        merchantDTO = dtuPay.getAccount(currentMerchantId);
        Assert.assertNotNull(merchantDTO);
    }

    @When("the merchant initiates a payment for {int} by the customer")
    public void the_merchant_initiates_a_payment_for_by_the_customer(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @And("the merchant asks for a token from the customer")
    public void the_merchant_asks_for_a_token_from_the_customer() {
        customerToken = merchantApp.requestTokenFromCustomer(currentCustomerId);
    }

    @Then("the merchant should receive a token")
    public void the_merchant_should_receive_a_token() {
        Assert.assertNotNull(customerToken);
    }

    @And("the payment is successful")
    public void the_payment_is_successful() {
        PaymentRequest paymentRequest = new PaymentRequest(currentCustomerId, currentMerchantId, paymentAmount, customerToken.getId(), false);
        successPayment = merchantApp.processPayment(paymentRequest);
        assertTrue(successPayment);
    }

    @Then("the customer should have a balance of {int} left")
    public void the_customer_should_have_a_balance_of_left(Integer balanceLeft) {
        customerDTO = dtuPay.getAccount(currentCustomerId);
        Assert.assertNotNull(customerDTO);
        Assert.assertEquals(BigDecimal.valueOf(balanceLeft).intValue(), customerDTO.getBankAccount().getBalance().intValue());
    }

    @Then("the merchant should have a balance of {int} left")
    public void the_merchant_should_have_a_balance_of_left(Integer balanceLeft) {
        merchantDTO = dtuPay.getAccount(currentMerchantId);
        Assert.assertNotNull(merchantDTO);
        Assert.assertEquals(BigDecimal.valueOf(balanceLeft).intValue(), merchantDTO.getBankAccount().getBalance().intValue());
    }

    @Then("the latest transaction contain the amount {int} for both accounts")
    public void the_latest_transaction_contain_the_amount_for_both_accounts(Integer transactionAmount) {
        MoneySummary moneySummary = reportClient.getAllTransactions();
        Comparator<TransactionDTO> comparator = Comparator.comparing(TransactionDTO::getTime);

        /*
         * To make the test work, we need to wait a little bit
         * before the report service has registered the previous payment
         */
        wait(200);

        latestTransaction = moneySummary.getTransactions().stream().max(comparator).get();
        Assert.assertNotNull(latestTransaction);
        Assert.assertEquals(BigDecimal.valueOf(transactionAmount).intValue(), latestTransaction.getAmount().intValue());
    }

    @And("the latest transaction related to the customer contain balance {int}")
    public void the_latest_transaction_related_to_the_customer_contain_balance(Integer balance) {
        Date startDate = Date.from(LocalDate.now().minusDays(1).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(7).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        /*
         * To make the test work, we need to wait a little bit
         * before the report service has registered the previous payment
         */
        wait(200);

        List<TransactionDTO> transactions = reportClient.getCustomerReport(currentCustomerId, startDate, endDate);
        Comparator<TransactionDTO> comparator = Comparator.comparing(TransactionDTO::getTime);

        latestTransaction = transactions.stream().max(comparator).get();
        Assert.assertNotNull(latestTransaction);
        Assert.assertEquals(BigDecimal.valueOf(balance).intValue(), latestTransaction.getBalance().intValue());
    }

    @And("the latest transaction related to the merchant should contain anonymized balance and debtor")
    public void the_latest_transaction_related_to_the_merchant_contain_balance() {
        Date startDate = Date.from(LocalDate.now().minusDays(1).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        Date endDate = Date.from(LocalDate.now().plusDays(7).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        /*
         * To make the test work, we need to wait a little bit
         * before the report service has registered the previous payment
         */
        wait(200);

        List<TransactionDTO> transactions = reportClient.getMerchantReport(currentMerchantId, startDate, endDate);
        Comparator<TransactionDTO> comparator = Comparator.comparing(TransactionDTO::getTime);

        latestTransaction = transactions.stream().max(comparator).get();

        Assert.assertNotNull(latestTransaction);
        assertNull(latestTransaction.getBalance());
        assertNull(latestTransaction.getDebtor());
    }

    @And("an invalid token {string}")
    public void an_invalid_token(String token) {
        customerToken = new Token(token);
    }

    @Given("a customer with id {string} and a token {string} where neither exist")
    public void a_customer_with_id_and_a_token_where_neither_exist(String id, String token) {
        this.currentCustomerId = id;
        this.customerToken = new Token(token);
    }

    @And("a merchant that exists in the system")
    public void a_merchant_that_exists_in_the_system() {
        currentMerchantId = merchantDTO.getId();
    }


    @Then("the payment is unsuccessful")
    public void the_payment_is_unsuccessful() {
        PaymentRequest paymentRequest = new PaymentRequest(currentCustomerId, currentMerchantId, paymentAmount, customerToken.getId(), false);
        successPayment = merchantApp.processPayment(paymentRequest);
        assertFalse(successPayment);
    }

    @Given("a customer that exists in the system")
    public void a_customer_that_exist_in_the_system() {
        this.currentCustomerId = createdCustomerId;
    }

    @Given("a merchant with id {string} that does not exist in the system")
    public void a_merchant_with_id_that_does_not_exist_in_the_system(String id) {
        this.currentMerchantId = id;
    }

    private void wait(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("the merchant initiates a refund for {int} by the customer")
    public void the_merchant_initiates_a_refund_for_by_the_customer(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Then("the refund is successful")
    public void the_refund_is_successful() {
        PaymentRequest paymentRequest = new PaymentRequest(currentCustomerId, currentMerchantId, paymentAmount, customerToken.getId(), true);
        successPayment = merchantApp.refund(paymentRequest);
        assertTrue(successPayment);
    }

}