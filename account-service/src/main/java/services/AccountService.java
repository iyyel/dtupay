/**
 * @author Kasper (s141250)
 * @author Sebastian (s135243)
 */

package services;

import dto.AccountInformation;
import dto.BankAccountDTO;
import dto.UserAccountDTO;
import dto.UserRegistrationDTO;
import exceptions.account.AccountExistsException;
import exceptions.account.AccountNotFoundException;
import exceptions.account.AccountRegistrationException;
import exceptions.account.BankAccountException;
import infrastructure.bank.*;
import infrastructure.repositories.AccountRepository;
import infrastructure.repositories.interfaces.IAccountRepository;
import services.interfaces.IAccountService;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AccountService implements IAccountService {

    private final BankService bs = new BankServiceService().getBankServicePort();
    private final IAccountRepository repo = AccountRepository.getInstance();

    public void clear() {
        repo.clear();
    }

    @Override
    public String register(UserRegistrationDTO creationRequest)
            throws AccountExistsException {

        if (isRegistered(creationRequest)) {
            throw new AccountExistsException("Account with cpr (" + creationRequest.getCprNumber() + ") already exists!");
        }

        // create or retrieve bank account information for the user
        String bankId = null;
        try {
            bankId = registerBankAccount(creationRequest);
        } catch (BankAccountException e) { }

        if (bankId == null) {
            // we didn't manage to create the user, try to fetch it
            try {
                Account a = getBankAccountByCpr(creationRequest.getCprNumber());
                bankId = a.getId();
            } catch (BankAccountException e) { }
        }

        AccountInformation newAccount = new AccountInformation();
        newAccount.setId(UUID.randomUUID().toString());
        newAccount.setBankId(bankId);
        newAccount.setCpr(creationRequest.getCprNumber());

        repo.add(newAccount);

        return newAccount.getId();
    }

    @Override
    public UserAccountDTO get(String id) throws AccountNotFoundException {
        AccountInformation accountInformation = repo.getById(id);

        if (accountInformation == null) {
            throw new AccountNotFoundException("Account with id (" + id + ") is not found!");
        }

        try {
            return getUserAccountFromInfo(accountInformation);
        } catch (BankAccountException e) {
            throw new AccountNotFoundException("Account with id (" + id + ") is not found!");
        }
    }

    @Override
    public UserAccountDTO getByCpr(String cpr) throws AccountNotFoundException {
        AccountInformation accountInformation = repo.getByCpr(cpr);

        if (accountInformation == null) {
            throw new AccountNotFoundException("Account with cpr (" + cpr + ") is not found!");
        }

        try {
            return getUserAccountFromInfo(accountInformation);
        } catch (BankAccountException e) {
            throw new AccountNotFoundException("Account with cpr (" + cpr + ") is not found!");
        }
    }

    @Override
    public List<UserAccountDTO> getAll() throws BankAccountException {
        List<UserAccountDTO> userAccountDTOs = new ArrayList<>();
        for (AccountInformation accountInfo : repo.getAll()) {
            userAccountDTOs.add(getUserAccountFromInfo(accountInfo));
        }
        return userAccountDTOs;
    }

    @Override
    public void retireAccountByCpr(String cpr) throws BankAccountException {
        AccountInformation accountInformation = repo.getByCpr(cpr);

        if (accountInformation == null) { // user doesn't exist
            return;
        }

        retireAccountFromInfo(accountInformation);
    }

    @Override
    public void retireAccount(String id) throws BankAccountException {
        AccountInformation accountInformation = repo.getById(id);

        if (accountInformation == null) { // user doesn't exist
            return;
        }

        retireAccountFromInfo(accountInformation);
    }

    private boolean isRegistered(UserRegistrationDTO userRegistrationDTO) {
        AccountInformation accountInformation = repo.getByCpr(userRegistrationDTO.getCprNumber());
        return accountInformation != null;
    }

    private String registerBankAccount(UserRegistrationDTO userRegistrationDTO)
            throws BankAccountException {
        BigDecimal initialBalance = userRegistrationDTO.getBankAccount().getBalance();
        User user = new User();
        user.setCprNumber(userRegistrationDTO.getCprNumber());
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());

        // try to create a bank account for the user
        String bankId;
        try {
            bankId = bs.createAccountWithBalance(user, initialBalance);
        } catch (BankServiceException_Exception e) {
            throw new BankAccountException("Failed to create bank account" +
                    " for account with cpr (" + userRegistrationDTO.getCprNumber() + ")");
        }

        return bankId;
    }

    private Account getBankAccountByCpr(String cpr) throws BankAccountException {
        try {
            return bs.getAccountByCprNumber(cpr);
        } catch (BankServiceException_Exception e) {
            throw new BankAccountException(e.getMessage());
        }
    }

    private Account getBankAccount(String bankId) throws BankAccountException {
        try {
            return bs.getAccount(bankId);
        } catch (BankServiceException_Exception e) {
            throw new BankAccountException(e.getMessage());
        }
    }

    private UserAccountDTO getUserAccountFromInfo(AccountInformation accountInformation)
            throws BankAccountException {
        Account bankAccount = getBankAccount(accountInformation.getBankId());

        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setId(accountInformation.getId());
        userAccountDTO.setFirstName(bankAccount.getUser().getFirstName());
        userAccountDTO.setLastName(bankAccount.getUser().getLastName());
        userAccountDTO.setCpr(bankAccount.getUser().getCprNumber());

        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setId(accountInformation.getBankId());
        bankAccountDTO.setBalance(bankAccount.getBalance());

        userAccountDTO.setBankAccount(bankAccountDTO);

        return userAccountDTO;
    }

    private void retireAccountFromInfo(AccountInformation accountInformation)
            throws BankAccountException {
        try {
            bs.retireAccount(accountInformation.getBankId());
            repo.remove(accountInformation.getId());
        } catch (BankServiceException_Exception e) {
            throw new BankAccountException(e.getMessage());
        }
    }
}
