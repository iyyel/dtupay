package mock;

import infrastructure.bank.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockBank implements BankService {

    private List<Account> accounts;
    private List<Transaction> transactions;
    public boolean haveBeenNotified = false;

    public MockBank() {
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    @Override
    public Account getAccount(String accountId) throws BankServiceException_Exception {
        Account acc = accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .findAny()
                .orElse(null);

        if (acc == null) {
            throw new BankServiceException_Exception("Account with accountId (" + accountId + ") is not found!",
                    new BankServiceException());
        }

        return acc;
    }

    @Override
    public Account getAccountByCprNumber(String cpr) throws BankServiceException_Exception {
        Account acc = accounts.stream()
                .filter(account -> account.getUser().getCprNumber().equals(cpr))
                .findAny()
                .orElse(null);

        if (acc == null) {
            throw new BankServiceException_Exception("Account with cpr (" + cpr + ") is not found!",
                    new BankServiceException());
        }

        return acc;
    }

    @Override
    public String createAccountWithBalance(User user, BigDecimal balance) throws BankServiceException_Exception {
        Account acc = new Account();
        acc.setUser(user);
        acc.setBalance(balance);
        accounts.add(acc);
        return null; // THIS IS WRONG. :)
    }

    @Override
    public void retireAccount(String accountId) throws BankServiceException_Exception {

    }

    @Override
    public List<AccountInfo> getAccounts() {
        return null;
    }

    @Override
    public void transferMoneyFromTo(String debtor, String creditor, BigDecimal amount, String description) throws BankServiceException_Exception {
        haveBeenNotified = true;
        if(amount.equals(new BigDecimal(-1))){
                throw new BankServiceException_Exception("fail", new BankServiceException());
        }
    }

}