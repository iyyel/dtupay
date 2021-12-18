package cucumber;

import com.client.DTUPayClient;
import dto.BankRegistrationDTO;
import dto.UserAccountDTO;
import dto.UserRegistrationDTO;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * primary-author: Troels (s161791)
 * co-author: Daniel (s151641)
 */
public class AccountSteps {

    private final DTUPayClient dtuPay = new DTUPayClient();

    private UserRegistrationDTO userRegistrationDTO;

    private String currentAccountId;
    private String createdAccountId;

    private UserAccountDTO accountDTO;
    private List<UserAccountDTO> allAccounts;
    private boolean successfulRetirement;

    @After
    public void cleanup() {
        dtuPay.retireAccount(createdAccountId);
    }

    @Given("a new account with cpr {string}, first name {string}, last name {string} and an initial balance of {int}")
    public void a_new_account_with_cpr_first_name_last_name_and_an_initial_balance_of(String cpr, String firstName, String lastName, Integer balance) {
        userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setBankAccount(new BankRegistrationDTO());

        userRegistrationDTO.setCprNumber(cpr);
        userRegistrationDTO.setFirstName(firstName);
        userRegistrationDTO.setLastName(lastName);
        userRegistrationDTO.getBankAccount().setBalance(BigDecimal.valueOf(balance));
    }

    @When("the account is registered")
    public void the_account_is_registered() {
        currentAccountId = dtuPay.registerAccount(userRegistrationDTO);
        createdAccountId = currentAccountId;
    }

    @Then("the registration should be successful")
    public void the_registration_should_be_successful() {
        assertNotNull(currentAccountId);
    }

    @And("the new account should exist in the system with correct information")
    public void the_new_account_should_exist_in_the_system_with_the_information() {
        accountDTO = dtuPay.getAccount(currentAccountId);
        Assert.assertNotNull(accountDTO);
        assertEquals(createdAccountId, accountDTO.getId());
        assertEquals(userRegistrationDTO.getCprNumber(), accountDTO.getCpr());
        assertEquals(userRegistrationDTO.getFirstName(), accountDTO.getFirstName());
        assertEquals(userRegistrationDTO.getLastName(), accountDTO.getLastName());
        assertEquals(userRegistrationDTO.getBankAccount().getBalance().intValue(), accountDTO.getBankAccount().getBalance().intValue());
    }

    @When("the account is registered again")
    public void the_account_is_registered_again() {
        currentAccountId = dtuPay.registerAccount(userRegistrationDTO);
    }

    @Then("the registration should be unsuccessful")
    public void the_registration_should_be_unsuccessful() {
        Assert.assertNull(currentAccountId);
    }

    @When("account information is retrieved by cpr")
    public void account_information_is_retrieved_by_cpr() {
        accountDTO = dtuPay.getAccountByCpr(accountDTO.getCpr());
        currentAccountId = accountDTO.getId();
    }

    @Then("the retrieval should be successful")
    public void the_retrieval_should_be_successful() {
        Assert.assertNotNull(accountDTO);
    }

    @Then("the retrieved information should be correct")
    public void the_retrieved_information_should_be_correct() {
        assertEquals(createdAccountId, accountDTO.getId());
        assertEquals(userRegistrationDTO.getCprNumber(), accountDTO.getCpr());
        assertEquals(userRegistrationDTO.getFirstName(), accountDTO.getFirstName());
        assertEquals(userRegistrationDTO.getLastName(), accountDTO.getLastName());
        assertEquals(userRegistrationDTO.getBankAccount().getBalance().intValue(), accountDTO.getBankAccount().getBalance().intValue());
    }

    @When("account information is retrieved by id {string} that does not exist")
    public void account_information_is_retrieved_by_id_that_does_not_exist(String id) {
        accountDTO = dtuPay.getAccount(id);
    }

    @Then("the retrieval should be unsuccessful")
    public void the_retrieval_should_be_unsuccessful() {
        Assert.assertNull(accountDTO);
    }

    @When("account information is retrieved by cpr {string} that does not exist")
    public void account_information_is_retrieved_by_cpr_that_does_not_exist(String cpr) {
        accountDTO = dtuPay.getAccountByCpr(cpr);
    }

    @When("a customer retires their account by cpr")
    public void a_customer_retires_their_account_by_cpr() {
        successfulRetirement = dtuPay.retireAccountByCpr(accountDTO.getCpr());
    }

    @Then("the retirement should be successful")
    public void the_retirement_should_be_successful() {
        assertTrue(successfulRetirement);
    }

    @When("a customer retires their account by cpr with cpr {string}")
    public void a_customer_retires_their_account_by_cpr_with_cpr(String cpr) {
        successfulRetirement = dtuPay.retireAccountByCpr(cpr);
    }

    @When("a customer retires their account by id with id {string}")
    public void a_customer_retires_their_account_by_id_with_id(String id) {
        successfulRetirement = dtuPay.retireAccount(id);
    }

    @When("a customer requests all account information from DTUPay")
    public void a_person_requests_all_account_information_from_dtu_pay() {
       allAccounts = dtuPay.getAllAccounts();
    }

    @Then("the request should be successful")
    public void the_request_should_be_successful() {
        assertNotNull(allAccounts);
    }

    @Then("should contain the right amount of accounts")
    public void should_contain_the_right_amount_of_accounts() {
        assertEquals(1, allAccounts.size());
    }

}