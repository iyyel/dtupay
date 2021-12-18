/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 *
 * Handles incoming REST calls and sends to business logic.
 */

package services;

import dto.MoneySummary;
import dto.TransactionDTO;
import messaging.Event;
import messaging.EventReceiver;
import messaging.EventSender;
import services.interfaces.IReportService;

import java.util.Date;
import java.util.List;

public class ReportReceiverService implements EventReceiver {

    IReportService rs;
    EventSender eventSender;

    public ReportReceiverService(EventSender eventSender, ReportService reportService) {
        this.eventSender = eventSender;
        this.rs = reportService;
    }

    public List<TransactionDTO> requestAllTransactions() {
        return rs.getRepo().getAll();
    }

    public MoneySummary requestSummary() {
        return rs.getSummary();
    }

    public List<TransactionDTO> requestAllCustomerTransactions(String customerId) {
        return rs.customerReport(customerId);
    }

    public List<TransactionDTO> requestAllCustomerTransactionsBetween(String customerId, Date beg, Date end) throws Exception {
        return rs.customerReport(customerId, beg, end);
    }

    public List<TransactionDTO> requestAllMerchantTransactions(String merchantId) {
        return rs.merchantReport(merchantId);
    }

    public List<TransactionDTO> requestAllMerchantTransactionsBetween(String merchantId, Date beg, Date end) throws Exception {
        return rs.merchantReport(merchantId, beg, end);
    }

    @Override
    public void receiveEvent(Event event) {

    }
}
