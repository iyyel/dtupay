/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 */

package services.interfaces;

import dto.MoneySummary;
import dto.TransactionDTO;
import exceptions.transaction.TransactionException;
import infrastructure.repositories.interfaces.ITransactionRepository;
import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportService {
    List<TransactionDTO> customerReport(String customerId, Date date, Date endDateFixed) throws ParseException;
    MoneySummary getSummary();

    List<TransactionDTO> customerReport(String customerId);

    List<TransactionDTO> merchantReport(String merchantId, Date date, Date endDateFixed) throws ParseException;

    List<TransactionDTO> merchantReport(String merchantId);

    Map<String, BigDecimal> requestSummary(List<TransactionDTO> transactions) throws Exception;
    List<TransactionDTO> requestAllCustomerTransactions(List<TransactionDTO> transactions, String customerId);
    List<TransactionDTO> requestAllCustomerTransactionsBetween(List<TransactionDTO> transactions, String customerId, Date beg, Date end) throws DatatypeConfigurationException, ParseException;
    ITransactionRepository getRepo();
    List<TransactionDTO> requestAllMerchantTransactions(List<TransactionDTO> transactions, String merchantId);
    List<TransactionDTO> requestAllMerchantTransactionsBetween(List<TransactionDTO> transactions, String merchantId, Date beg, Date end) throws DatatypeConfigurationException, ParseException;

    void addToRepo(TransactionDTO transaction) throws TransactionException;
}
