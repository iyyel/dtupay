/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 *
 * Main service for reporting, handles business logic.
 */

package services;

import dto.MoneySummary;
import dto.TransactionDTO;
import exceptions.transaction.TransactionException;
import infrastructure.repositories.TransactionRepository;
import infrastructure.repositories.interfaces.ITransactionRepository;
import services.interfaces.IReportService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.RoundingMode.HALF_UP;

public class ReportService implements IReportService {

    private static final ITransactionRepository repo = TransactionRepository.getInstance();

    @Override
    public MoneySummary getSummary() {
        List<TransactionDTO> list = repo.getAll();
        System.out.println(list);
        return new MoneySummary(requestSummary(list), list);
    }

    @Override
    public List<TransactionDTO> customerReport(String customerId, Date startDate, Date endDate) {
        List<TransactionDTO> list = repo.getAll();
        return requestAllCustomerTransactionsBetween(list, customerId, startDate, endDate);
    }

    @Override
    public List<TransactionDTO> customerReport(String customerId) {
        List<TransactionDTO> list = repo.getAll();
        return requestAllCustomerTransactions(list, customerId);
    }

    @Override
    public List<TransactionDTO> merchantReport(String merchantId, Date startDate, Date endDate) {
        List<TransactionDTO> list = repo.getAll();
        return requestAllMerchantTransactionsBetween(list, merchantId, startDate, endDate);
    }

    @Override
    public List<TransactionDTO> merchantReport(String merchantId) {
        List<TransactionDTO> list = repo.getAll();
        return requestAllMerchantTransactions(list, merchantId);
    }

    @Override
    public Map<String, BigDecimal> requestSummary(List<TransactionDTO> transactions) {
        Map<String, BigDecimal> summary = new HashMap<>();
        BigDecimal min = null, max = null, sum = null, mean = null;
        for (TransactionDTO t : transactions) {
            if (min == null) {
                min = t.getAmount();
                max = t.getAmount();
                sum = t.getAmount();
            } else {
                min = min.min(t.getAmount());
                max = max.max(t.getAmount());
                sum = sum.add(t.getAmount());
            }
        }
        if (sum != null) {
            mean = sum.divide(new BigDecimal(transactions.size()), HALF_UP);
        }
        summary.put("min", min);
        summary.put("max", max);
        summary.put("mean", mean);
        summary.put("sum", sum);
        return summary;
    }

    @Override
    public List<TransactionDTO> requestAllCustomerTransactions(List<TransactionDTO> transactions, String customerId) {
        transactions.removeIf(obj -> !obj.getDebtor().equals(customerId));
        return transactions;
    }

    @Override
    public List<TransactionDTO> requestAllCustomerTransactionsBetween(List<TransactionDTO> transactions, String customerId, Date beg, Date end) {

        transactions.removeIf(obj -> obj.getTime() == null);
        transactions.removeIf(obj -> !obj.getDebtor().equals(customerId) || obj.getTime().compareTo(beg) < 0 || obj.getTime().compareTo(end) > 0);
        return transactions;
    }

    @Override
    public ITransactionRepository getRepo() {
        return repo;
    }

    private List<TransactionDTO> deepCopyTransactions(List<TransactionDTO> transactions) {
        List<TransactionDTO> copyList = new ArrayList<>();
        for (TransactionDTO t : transactions)
            copyList.add(new TransactionDTO(t));
        return copyList;
    }

    @Override
    public List<TransactionDTO> requestAllMerchantTransactions(List<TransactionDTO> transactions, String merchantId) {
        List<TransactionDTO> transactionsCopy = deepCopyTransactions(transactions);

        transactionsCopy.removeIf(obj -> !obj.getCreditor().equals(merchantId));
        for (TransactionDTO transaction : transactionsCopy) {
            transaction.setDebtor(null);
            transaction.setBalance(null);
        }
        return transactionsCopy;
    }

    @Override
    public List<TransactionDTO> requestAllMerchantTransactionsBetween(List<TransactionDTO> transactions, String merchantId, Date beg, Date end) {
        List<TransactionDTO> transactionsCopy = deepCopyTransactions(transactions);
        transactionsCopy.removeIf(obj -> obj.getTime() == null);
        transactionsCopy.removeIf(obj -> !obj.getCreditor().equals(merchantId) || obj.getTime().compareTo(beg) < 0 || obj.getTime().compareTo(end) > 0);

        for (TransactionDTO transaction : transactionsCopy) {
            transaction.setDebtor(null);
            transaction.setBalance(null);
        }

        return transactionsCopy;
    }

    @Override
    public void addToRepo(TransactionDTO transaction) throws TransactionException {
        if (transaction.getAmount() != null && transaction.getDebtor() != null && transaction.getCreditor() != null) {
            repo.add(transaction);
        } else {
            throw new TransactionException("Transaction formed incorrectly");
        }
    }
}
