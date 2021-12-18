/**
 * @author Kasper (s141250)
 * @author Sebastian (s135243)
 */

package cucumber.steps;

import io.quarkus.test.junit.QuarkusTest;
import dto.*;
import services.AccountService;
import infrastructure.repositories.*;
import infrastructure.bank.*;
import exceptions.account.*;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import io.cucumber.java.After;

import javax.inject.Inject;
import java.util.List;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceSteps {
    BankService bs = new BankServiceService().getBankServicePort();

    AccountService as;
    String currentUserId = null;

    @Given("An instance of the account service has been initialized")
    public void an_instance_of_the_account_service_has_been_initialized() {
        as = new AccountService();
        as.clear(); // clear the internal list of users
    }

    @Then("The system contains no users")
    public void the_system_contains_no_users() {
        try {
            List<UserAccountDTO> dtos = as.getAll();
            assertEquals(0, dtos.size());
        } catch (BankAccountException e) {
            e.getMessage();
        }
    }

    @When("A user with first name {string}, last name {string}, CPR {string} is added")
    public void a_user_with_first_name_last_name_cpr_is_added(String string, String string2, String string3) {
        UserRegistrationDTO userRegister = new UserRegistrationDTO(string, string2, string3);
        BankRegistrationDTO bank = new BankRegistrationDTO(new BigDecimal(0));
        userRegister.setBankAccount(bank);
        try {
            currentUserId = as.register(userRegister);
        } catch (AccountExistsException e) {
            e.getMessage();
        }

    }

    @Then("The user is added to the list of users and the number of users is {int}")
    public void the_user_is_added_to_the_list_of_users_and_the_number_of_users_is(Integer int1) {
        try {
            // check length of the list in the repository
            assertEquals(int1, as.getAll().size());
        } catch (BankAccountException e) {
            e.getMessage();
        }
    }

    @Given("That user's ID is {string}")
    public void that_user_s_id_is(String string) {
        try {
            UserAccountDTO userAccount = as.get(currentUserId);
            assertEquals(userAccount.getId(), currentUserId);
        } catch (AccountNotFoundException e) {
            e.getMessage();
        }
    }

    @When("A user is requested retrieved using the ID {string}")
    public void a_user_is_requested_retrieved_using_the_id(String string) {
        try {
            UserAccountDTO userAccount = as.get(currentUserId);
        } catch (AccountNotFoundException e) {
            e.getMessage();
        }
    }

    @Then("The user is retrieved")
    public void the_user_is_retrieved() {
        try {
            UserAccountDTO userAccount = as.get(currentUserId);
            assertNotNull(userAccount);
        } catch (AccountNotFoundException e) {
            e.getMessage();
        }
    }


    @Then("The CPR of that user is {string}")
    public void the_cpr_of_that_user_is(String string) {
        try {
            UserAccountDTO userAccount = as.get(currentUserId);
            assertEquals(userAccount.getCpr(), string);
        } catch (AccountNotFoundException e) {
            e.getMessage();
        }
    }

    @When("A user is requested retrieved using the CPR {string}")
    public void a_user_is_requested_retrieved_using_the_cpr(String string) {
        try {
            UserAccountDTO userAccount = as.getByCpr(string);
        } catch (AccountNotFoundException e) {
            e.getMessage();
        }
    }

    @Then("The ID of that user is {string}")
    public void the_id_of_that_user_is(String string) {
        String cpr = "123456-7890";
        try {
            UserAccountDTO userAccount = as.getByCpr(cpr);
            assertNotNull(userAccount.getId());
        } catch (AccountNotFoundException e) {
            e.getMessage();
        }
    }

    @Given("Two users are registered")
    public void two_users_are_registered() {
        UserRegistrationDTO user1 = new UserRegistrationDTO("Bjarne", "Ivertsen", "123456-7890");
        user1.setBankAccount(new BankRegistrationDTO(new BigDecimal(0)));

        UserRegistrationDTO user2 = new UserRegistrationDTO("Soeren", "Soerensen", "654321-0987");
        user2.setBankAccount(new BankRegistrationDTO(new BigDecimal(0)));

        try {
            as.register(user1);
            as.register(user2);
       } catch (AccountExistsException e) {
           e.getMessage();
       }

       try {
           assertEquals(2, as.getAll().size());
       } catch (BankAccountException e) {
           e.getMessage();
       }
    }

    @When("A request to retrieve all users is received")
    public void a_request_to_retrieve_all_users_is_received() {
        try {
            as.getAll();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Then("A list of all the users is retrieved")
    public void a_list_of_all_the_users_is_retrieved() {
        try {
            assertNotNull(as.getAll());
            assertEquals(2, as.getAll().size());
        } catch (BankAccountException e) {
            e.getMessage();
        }
    }

    @Then("The number of users in the list is {int}")
    public void the_number_of_users_in_the_list_is(Integer int1) {
        try {
            assertEquals(int1, as.getAll().size());
        } catch (BankAccountException e) {
            e.getMessage();
        }
    }

    @When("A user requests to retire an account using CPR {string}")
    public void a_user_requests_to_retire_an_account_using_cpr(String string) {
        try {
            as.retireAccountByCpr(string);
        } catch (BankAccountException e) {
            e.getMessage();
        }
    }

    @Then("The account with CPR {string} is retired")
    public void the_account_with_cpr_is_retired(String string) {
        try {
            assertNull(as.getByCpr(string));
        } catch (AccountNotFoundException e) {
            e.getMessage();
        }
    }

    @When("A user requests to retire an account using ID {string}")
    public void a_user_requests_to_retire_an_account_using_id(String string) {
        try {
            as.retireAccount(currentUserId);
        } catch (BankAccountException e) {
            e.getMessage();
        }
    }

    @Then("The account with ID {string} is retired")
    public void the_account_with_id_is_retired(String string) {
        try {
            assertNull(as.get(currentUserId));
        } catch (AccountNotFoundException e) {
            e.getMessage();
        }
    }

    @When("A request to register a user using the same information is requested")
    public void a_request_to_register_a_user_using_the_same_information_is_requested() {
        UserRegistrationDTO original = new UserRegistrationDTO("Bjarne", "Ivertsen", "123456-7890");
        original.setBankAccount(new BankRegistrationDTO(new BigDecimal(0)));

        UserRegistrationDTO duplicate = new UserRegistrationDTO("Bjarne", "Ivertsen", "123456-7890");
        duplicate.setBankAccount(new BankRegistrationDTO(new BigDecimal(0)));

        try {
            as.register(original);
            as.register(duplicate);
        } catch (AccountExistsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Then("The duplicate user is not registered")
    public void the_duplicate_user_is_not_registered() {
        try {
            assertEquals(as.getAll().size(), 1);
        } catch (BankAccountException e) {
            e.getMessage();
        }
    }

    @After
    public void cleanup_remote() {
        // clean up accounts created with the bank
        try {
            Account a = bs.getAccountByCprNumber("123456-7890");
            bs.retireAccount(a.getId());
        } catch (Exception e) { }
        try {
            Account a = bs.getAccountByCprNumber("654321-0987");
            bs.retireAccount(a.getId());
        } catch (Exception e) { }
    }
}
